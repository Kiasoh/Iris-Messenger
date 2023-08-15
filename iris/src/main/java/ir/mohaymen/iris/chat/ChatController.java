package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.auth.AuthService;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/create-chat")
    public ResponseEntity<GetChatDto> createChat(@RequestBody CreateChatDto createChatDto) {
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
        if (!chatService.isInChat(chat , getUserByToken()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        GetChatDto getChatDto = modelMapper.map(chat , GetChatDto.class);
        getChatDto.setSubCount(chat.getSubs().size());
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }
    @GetMapping("/get-all-chats")
    public ResponseEntity<List<MenuChatDto>> getAllChats () {
        List<MenuChatDto> menuChatDtos = new ArrayList<>();
        for (Subscription sub :getUserByToken().getSubs()) {
            MenuChatDto menuChatDto = modelMapper.map(sub.getChat() , MenuChatDto.class);
            if (sub.getChat().getChatProfiles().size() != 0)
                menuChatDto.setMedia(sub.getChat().getChatProfiles().get(sub.getChat().getChatProfiles().size() - 1) .getMedia());
            if (sub.getChat().getMessages().size() != 0) {
                menuChatDto.setLastMessage(sub.getChat().getMessages().get(sub.getChat().getMessages().size() - 1).getText());
                menuChatDto.setUserFirstName(sub.getChat().getMessages().get(sub.getChat().getMessages().size() - 1).getSender().getFirstName());
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
