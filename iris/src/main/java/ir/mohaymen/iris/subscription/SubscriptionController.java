package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubscriptionController extends BaseController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ChatService chatService;
//    public SubscriptionController(SubscriptionService subscriptionService) {this.subscriptionService = subscriptionService;}
    @RequestMapping("/add-user-to-chat")
    public ResponseEntity<Chat> addToChat(@RequestBody SubDto subDto) {
        try {
            Chat chat = chatService.getById(subDto.getChatId());

            for (Long id: subDto.getUserIds()){
                if (chat.getSubs().size() > 1 && chat.getChatType()== ChatType.PV)
                    throw new Exception();
                subscriptionService.createOrUpdate(new Subscription(null , userService.getById (id) ,chat));
            }

        }
        catch (Exception e) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(403));
        }
        return new ResponseEntity<>(chatService.getById(subDto.getChatId()) , HttpStatus.OK);
    }
}
