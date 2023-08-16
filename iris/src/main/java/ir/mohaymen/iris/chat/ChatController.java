package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.auth.AuthService;
import ir.mohaymen.iris.contact.ContactService;
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
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController extends BaseController {
    private final ChatService chatService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final ContactService contactService;

    @PostMapping("/create-chat")
    public ResponseEntity<GetChatDto> createChat(@RequestBody @Valid CreateChatDto createChatDto) {
        Chat chat = modelMapper.map(createChatDto, Chat.class);
        chat.setSubs(new HashSet<>());
        chat = chatService.createOrUpdate(chat);
        Subscription sub = new Subscription();
        sub.setChat(chat);
        sub.setUser(getUserByToken());
        subscriptionService.createOrUpdate(sub);
        for (Long id : createChatDto.getUserIds()) {
            chat = chatService.getById(chat.getChatId());
            User user = userService.getById(id);
            if ((chat.getSubs().size() > 1 && chat.getChatType() == ChatType.PV) || chatService.isInChat(chat, user))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            try {
                sub = new Subscription();
                sub.setChat(chat);
                sub.setUser(user);
                subscriptionService.createOrUpdate(sub);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        return getGetChatDtoResponseEntity(chat);
    }

    @GetMapping("/get-chat/{id}")
    public ResponseEntity<GetChatDto> getChat(@PathVariable Long id) {
        Chat chat = chatService.getById(id);
        return getGetChatDtoResponseEntity(chat);
    }

    @GetMapping("/{link}")
    public ResponseEntity<GetChatDto> getChatByLink(@PathVariable String link) {
        if (link == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Chat chat = chatService.getByLink(link);
        return getGetChatDtoResponseEntity(chat);
    }

    private ResponseEntity<GetChatDto> getGetChatDtoResponseEntity(Chat chat) {
        if (!chatService.isInChat(chat, getUserByToken()) || (!chat.isPublic() && chat.getChatType() != ChatType.PV))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        GetChatDto getChatDto = new GetChatDto();
        if ( chat.getChatType() != ChatType.PV){
            getChatDto = modelMapper.map(chat, GetChatDto.class);
            List<ProfileDto> profileDtoList = new ArrayList<>();
            chat.getChatProfiles().stream().forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
            getChatDto.setProfileDtoList(profileDtoList);
            getChatDto.setSubCount(chat.getSubs().size());
        }
        else {
            User user = userService.getById(chatService.helloFromTheOtherSide(chat , getUserByToken().getUserId()));
            getChatDto.setChatId(chat.getChatId());
            getChatDto.setSubCount(2);
            List<ProfileDto> profileDtoList = new ArrayList<>();
            user.getProfiles().stream().forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
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
                Nameable nameable = subscriptionService.setName(user.getContacts(), user);
                if (user.getProfiles().size() != 0) {
                    menuChatDto.setMedia(user.getProfiles().get(user.getProfiles().size() - 1).getMedia());
                    menuChatDto.setTitle(nameable.getFirstName() + " " + nameable.getLastName());
                }
                menuChatDto.setUserFirstName(nameable.getFirstName());
            }
            menuChatDtos.add(menuChatDto);
        }
        List<MenuChatDto> sorted = menuChatDtos.stream()
                .sorted(Comparator.comparing(MenuChatDto::getSentAt))
                .collect(Collectors.toList());
        return new ResponseEntity<>(sorted, HttpStatus.OK);
    }

    @DeleteMapping("delete-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable Long id) throws Exception {
        if (!chatService.isInChat(chatService.getById(id), getUserByToken()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        chatService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}