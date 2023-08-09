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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController extends BaseController {
    private final ChatService chatService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ModelMapper modelMapper;
//    public ChatController(ChatService chatService) {this.chatService = chatService}
    @RequestMapping("/add-chat")
    public ResponseEntity<Chat> createChat(@RequestBody ChatDto chatDto) {
        Chat chat;
        try {
            chatService.createOrUpdate(chat = this.modelMapper.map(chatDto, Chat.class));
            subscriptionService.createOrUpdate(new Subscription(null ,  getUserByToken(), chat));
            for (Long id: chatDto.getUserIds() )
                subscriptionService.createOrUpdate(new Subscription(null , userService.getById (id) , chat));
        }
        catch (Exception e) {throw new HttpClientErrorException(HttpStatusCode.valueOf(403));}
        return new ResponseEntity<>(chat , HttpStatus.OK);
    }

}