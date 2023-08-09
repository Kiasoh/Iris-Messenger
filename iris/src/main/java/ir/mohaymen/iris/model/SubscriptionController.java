package ir.mohaymen.iris.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subs")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    public ChatController(SubscriptionService subscriptionService) {this.subscriptionService = subscriptionService;}
    @RequestMapping("/add-user")
    public ResponseEntity<Chat> addToChat(@RequestBody Chat chat) {
        try {
            chatService.save (chat);
        }
        catch (Exception e) {return new ResponseEntity<>(chat , HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(chat , HttpStatus.OK);
    }
}
