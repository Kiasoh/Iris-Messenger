package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
import ir.mohaymen.iris.profile.ProfileDto;
import ir.mohaymen.iris.profile.ProfileMapper;
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
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController extends BaseController {

    private final ChatService chatService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
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
        Chat savedChat = chatService.createOrUpdate(chat);
        Subscription sub = new Subscription() {{
            setChat(savedChat);
            setUser(getUserByToken());
        }};
        //TODO:Permission set to owner
        subscriptionService.createOrUpdate(sub);
        Set<Subscription> addedSubs = new HashSet<>();
        addedSubs.add(sub);
        if ((createChatDto.getUserIds().size() != 1 && savedChat.getChatType() == ChatType.PV))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        for (Long id : createChatDto.getUserIds()) {
            chat = chatService.getById(savedChat.getChatId());
            User user = userService.getById(id);
            if (chatService.isInChat(chat, user))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            try {
                sub = new Subscription();
                sub.setChat(chat);
                sub.setUser(user);
                subscriptionService.createOrUpdate(sub);
                addedSubs.add(sub);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        chat.setSubs(addedSubs);
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
        GetChatDto getChatDto = new GetChatDto();
        if (chat.getChatType() != ChatType.PV) {
            getChatDto = modelMapper.map(chat, GetChatDto.class);
            List<ProfileDto> profileDtoList = new ArrayList<>();
            if (chat.getChatProfiles() != null)
                chat.getChatProfiles().forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
            getChatDto.setProfileDtoList(profileDtoList);
            getChatDto.setSubCount(chat.getSubs().size());
        } else {
            User user = userService.getById(chatService.getOtherPVUser(chat, getUserByToken().getUserId()));
            getChatDto.setChatId(chat.getChatId());
            getChatDto.setSubCount(2);
            List<ProfileDto> profileDtoList = new ArrayList<>();
            user.getProfiles().forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
            getChatDto.setProfileDtoList(profileDtoList);
            getChatDto.setBio(user.getBio());
            getChatDto.setLink(chat.getLink());
            getChatDto.setPublic(chat.isPublic());
            getChatDto.setChatType(chat.getChatType());
        }
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }

    @GetMapping("/get-all-chats")
    public ResponseEntity<List<MenuChatDto>> getAllChats() {
        List<MenuChatDto> menuChatDtos = new ArrayList<>();
        for (Subscription sub : getUserByToken().getSubs()) {
            Chat chat = sub.getChat();
            MenuChatDto menuChatDto = modelMapper.map(chat, MenuChatDto.class);
            if (chat.getChatType() != ChatType.PV)
                if (chat.getChatProfiles().size() != 0)
                    menuChatDto.setMedia(chat.getChatProfiles().get(chat.getChatProfiles().size() - 1).getMedia());
            if (chat.getMessages().size() != 0) {
                List<Message> messages = chat.getMessages();
                menuChatDto.setUnSeenMessages(messageService.countUnSeenMessages(sub.getLastMessageSeenId(), chat.getChatId()));
                menuChatDto.setLastMessage(messages.get(messages.size() - 1).getText());
                menuChatDto.setSentAt(messages.get(messages.size() - 1).getSendAt());
                User user = messages.get(messages.size() - 1).getSender();
                Nameable nameable = subscriptionService.setName(getUserByToken().getContacts(), user);
                if (user.getProfiles().size() != 0) {
                    menuChatDto.setMedia(user.getProfiles().get(user.getProfiles().size() - 1).getMedia());
                    menuChatDto.setTitle(nameable.getFirstName() + " " + nameable.getLastName());
                }
                menuChatDto.setUserFirstName(nameable.getFirstName());
            } else {
                menuChatDto.setSentAt(chat.getCreatedAt());
                User user = userService.getById(chatService.getOtherPVUser(chat, getUserByToken().getUserId()));
                Nameable nameable = subscriptionService.setName(getUserByToken().getContacts(), user);
                if (user.getProfiles().size() != 0) {
                    menuChatDto.setMedia(user.getProfiles().get(user.getProfiles().size() - 1).getMedia());
                    menuChatDto.setTitle(nameable.getFirstName() + " " + nameable.getLastName());
                }
                menuChatDto.setUserFirstName(nameable.getFirstName());
            }
            menuChatDtos.add(menuChatDto);
        }
        menuChatDtos.sort((o1, o2) -> {
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

}