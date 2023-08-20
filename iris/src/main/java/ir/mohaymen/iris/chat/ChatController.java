package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
import ir.mohaymen.iris.profile.*;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController extends BaseController {

    private final ChatService chatService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ChatProfileService chatProfileService;
    private final MessageService messageService;
    private final ContactService contactService;
    private final UserProfileService userProfileService;
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @PostMapping("/create-chat")
    public ResponseEntity<GetChatDto> createChat(@RequestBody @Valid CreateChatDto createChatDto) {
        Chat chat = modelMapper.map(createChatDto, Chat.class);
        logger.info(MessageFormat.format("user with phone number:{0} wants to create chat ", getUserByToken().getPhoneNumber()));
        if (!chat.getChatType().equals(ChatType.PV) && chat.getTitle().isBlank()) {
            logger.error("non-PV chat must have title");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        chat.setCreatedAt(Instant.now());
        if (chat.getChatType() == ChatType.PV)
            chat.setPublic(false);
        createInternalSub(chat , getUserByToken());
        chat = chatService.createOrUpdate(chat);
        if ((createChatDto.getUserIds().size() != 1 && chat.getChatType() == ChatType.PV))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        for (Long id : createChatDto.getUserIds()) {
            User user = userService.getById(id);
            if (chatService.isInChat(chat, user))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            try {
                createInternalSub(chat, user);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        return getGetChatDtoResponseEntity(chat);
    }

    @GetMapping("/get-chat/{id}")
    public ResponseEntity<GetChatDto> getChat(@PathVariable Long id) {
        Chat chat = chatService.getById(id);
        if (!chatService.isInChat(chat, getUserByToken()) || (!chat.isPublic() && chat.getChatType() != ChatType.PV))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return getGetChatDtoResponseEntity(chat);
    }

    @GetMapping("/{link}")
    public ResponseEntity<GetChatDto> getChatByLink(@PathVariable String link) {
        if (link == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Chat chat = chatService.getByLink(link);
        if (!chatService.isInChat(chat, getUserByToken()) || (!chat.isPublic() && chat.getChatType() != ChatType.PV))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return getGetChatDtoResponseEntity(chat);
    }

    private ResponseEntity<GetChatDto> getGetChatDtoResponseEntity(Chat chat) {
        GetChatDto getChatDto = modelMapper.map(chat, GetChatDto.class);
        List<ProfileDto> profileDtoList = new ArrayList<>();
        if (chat.getChatType() != ChatType.PV) {
            List<ChatProfile> chatProfileList = chatProfileService.getByChat(chat);
            if (chatProfileList != null)
                chatProfileList.forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
        } else {
            User user = userService.getById(chatService.getOtherPVUser(chat, getUserByToken().getUserId()));
            List<UserProfile> userProfileList = userProfileService.getByUser(user);
            if (userProfileList != null)
               userProfileList.forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
        }
        getChatDto.setChatId(chat.getChatId());
        getChatDto.setProfileDtoList(profileDtoList);
        getChatDto.setSubCount(subscriptionService.subscriptionCount(chat.getChatId()));
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }

    public MenuChatDto createMenuChat(Subscription sub) {
        Chat chat = sub.getChat();
        MenuChatDto menuChatDto = modelMapper.map(chat, MenuChatDto.class);
        if (chat.getChatType() != ChatType.PV) {
            List<ChatProfile> chatProfileList = chatProfileService.getByChat(chat);
            if (chatProfileList.size() != 0)
                menuChatDto.setMedia(chatProfileList.get(chatProfileList.size() - 1).getMedia());
        }
        else {
            User user = userService.getById(chatService.getOtherPVUser(chat, getUserByToken().getUserId()));
            Nameable nameable = subscriptionService.setName(contactService.getContactByFirstUser(getUserByToken()), user);
            List<UserProfile> userProfileList = userProfileService.getByUser(user);
            menuChatDto.setTitle(nameable.fullName());
            if (userProfileList.size() != 0)
                menuChatDto.setMedia(userProfileList.get(userProfileList.size() - 1).getMedia());
            menuChatDto.setUserFirstName(nameable.getFirstName());
        }
        if (chat.getMessages().size() != 0) {
            Message message = messageService.getLastMessageByChatId(chat);
            menuChatDto.setUnSeenMessages(messageService.countUnSeenMessages(sub.getLastMessageSeenId(), chat.getChatId()));
            menuChatDto.setLastMessage(message.getText());
            menuChatDto.setSentAt(message.getSendAt());
            User user = message.getSender();
            Nameable nameable = subscriptionService.setName(contactService.getContactByFirstUser(getUserByToken()), user);
            if (user.getProfiles().size() != 0) {
                menuChatDto.setMedia(user.getProfiles().get(user.getProfiles().size() - 1).getMedia());
            }
            menuChatDto.setUserFirstName(nameable.getFirstName());
        }
        else {
            menuChatDto.setSentAt(chat.getCreatedAt());
        }
        return menuChatDto;
    }

    @GetMapping("/get-all-chats")
    public ResponseEntity<List<MenuChatDto>> getAllChats() {
        List<MenuChatDto> menuChatDtos = new ArrayList<>();
        for (Subscription sub : subscriptionService.getAllSubscriptionByUserId(getUserByToken().getUserId())) {
            menuChatDtos.add(createMenuChat(sub));
        }
        Collections.sort(menuChatDtos, (o1, o2) -> {
            if (o1.getSentAt().isBefore(o2.getSentAt()))
                return 1;
            return -1;
        });
        return new ResponseEntity<>(menuChatDtos, HttpStatus.OK);
    }

    @DeleteMapping("delete-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable Long id) throws Exception {
        if (!chatService.isInChat(chatService.getById(id), getUserByToken()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        chatService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private Subscription createInternalSub(Chat chat, User user) {
        Subscription sub = new Subscription();
        sub.setChat(chat);
        sub.setUser(user);
        subscriptionService.createOrUpdate(sub);
        return sub;
    }

}



