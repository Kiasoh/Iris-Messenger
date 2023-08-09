package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
//    public ChatController(ChatService chatService) {this.chatService = chatService}
    @RequestMapping("/add-chat")
    public ResponseEntity<Chat> createChat(@RequestBody ChatDto chatDto) {
        Chat chat;
        try {
            chatService.create (chat = ChetMapper.toChat(chatDto));
            // TODO: 8/9/2023
//            subscriptionService.create(new Subscription(null , user , chat));
            for (Long id: chatDto.getUserIds() )
                subscriptionService.create (new Subscription(null , userService.getById (id) , chat));
        }
        catch (Exception e) {return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(chat , HttpStatus.OK);
    }
}
