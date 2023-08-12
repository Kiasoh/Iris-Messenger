package ir.mohaymen.iris.chat;

import com.fasterxml.jackson.databind.deser.Deserializers;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController extends BaseController {
    private final ChatService chatService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/create-chat")
    public ResponseEntity<Chat> createChat(@RequestBody ChatDto chatDto) {
        Chat chat;
        chatService.createOrUpdate(chat = this.modelMapper.map(chatDto, Chat.class));
        subscriptionService.createOrUpdate(new Subscription(null, getUserByToken(), chat));
        for (Long id : chatDto.getUserIds()) {
            User user = userService.getById(id);
            if ((chat.getSubs().size() > 1 && chat.getChatType() == ChatType.PV) || isInChat(chat, user))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            subscriptionService.createOrUpdate(new Subscription(null, userService.getById(id), chat));
        }

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    public boolean isInChat(Chat chat, User user) {
        Set<Subscription> subs = chatService.getById(chat.getChatId()).getSubs();
        return subs.stream().anyMatch(s -> s.getUser().getUserId() == user.getUserId());
    }

}
