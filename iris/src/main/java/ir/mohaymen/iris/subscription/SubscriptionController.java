package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ChatService chatService;
//    public SubscriptionController(SubscriptionService subscriptionService) {this.subscriptionService = subscriptionService;}
    @RequestMapping("/add-user-to-chat")
    public ResponseEntity<Chat> addToChat(@RequestBody SubDto subDto) {
        try {
            for (Long id: subDto.getUserIds() )
                subscriptionService.createOrUpdate(new Subscription(null , userService.getById (id) , chatService.getById(subDto.getChatId())));
        }
        catch (Exception e) {return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(chat , HttpStatus.OK);
    }
}
