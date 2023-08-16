package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.auth.AuthService;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
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
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.EmptyStackException;
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
    @PostMapping("/create-chat")
    public ResponseEntity<GetChatDto> createChat(@RequestBody @Valid CreateChatDto createChatDto) {
        Chat chat = modelMapper.map(createChatDto, Chat.class);
        chat.setSubs(new HashSet<>());
        chat = chatService.createOrUpdate(chat);
        Subscription sub = new Subscription(); sub.setChat(chat); sub.setUser(getUserByToken());subscriptionService.createOrUpdate(sub);
        for (Long id : createChatDto.getUserIds()) {
            chat = chatService.getById(chat.getChatId());
            User user = userService.getById(id);
            if ((chat.getSubs().size() > 1 && chat.getChatType() == ChatType.PV) || chatService.isInChat(chat, user))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            try {
                sub = new Subscription(); sub.setChat(chat); sub.setUser(user);
                subscriptionService.createOrUpdate(sub);
            }
            catch (Exception ex) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
        }
        GetChatDto getChatDto = modelMapper.map(chat , GetChatDto.class);
        getChatDto.setSubCount(chat.getSubs().size());
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
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
        if (!chatService.isInChat(chat , getUserByToken()) && !chat.isPublic())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        GetChatDto getChatDto = modelMapper.map(chat , GetChatDto.class);
        getChatDto.setSubCount(chat.getSubs().size());
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }

    @GetMapping("/get-all-chats")
    public ResponseEntity<List<MenuChatDto>> getAllChats () {
        List<MenuChatDto> menuChatDtos = new ArrayList<>();
        for (Subscription sub :getUserByToken().getSubs()) {
            Chat chat = sub.getChat();
            MenuChatDto menuChatDto = modelMapper.map(chat , MenuChatDto.class);
            if (chat.getChatProfiles().size() != 0)
                menuChatDto.setMedia(chat.getChatProfiles().get(chat.getChatProfiles().size() - 1) .getMedia());
            if (chat.getMessages().size() != 0) {
                List<Message> messages = chat.getMessages();
                menuChatDto.setUnSeenMessages(messageService.countUnSeenMessages(sub.getLastMessageSeenId() , chat.getChatId()));
                menuChatDto.setLastMessage(messages.get(messages.size() - 1).getText());
                menuChatDto.setSentAt(messages.get(messages.size() - 1).getSendAt());
                User user = messages.get(messages.size() - 1).getSender();
                Nameable nameable = subscriptionService.setName(user.getContacts() , user);
                menuChatDto.setUserFirstName(nameable.getFirstName());
            }
            menuChatDtos.add(menuChatDto);
        }
        return new ResponseEntity<>(menuChatDtos , HttpStatus.OK);
    }
    @DeleteMapping("delete-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable Long id) throws Exception {
        if (!chatService.isInChat(chatService.getById(id), getUserByToken()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        chatService.deleteById(id);
        return new ResponseEntity<>(null , HttpStatus.OK);
    }
}
