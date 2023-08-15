package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubscriptionController extends BaseController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ChatService chatService;

    // public SubscriptionController(SubscriptionService subscriptionService)
    // {this.subscriptionService = subscriptionService;}
    @PostMapping("/add-user-to-chat")
    public ResponseEntity<Chat> addToChat(@RequestBody AddSubDto addSubDto) {

        Chat chat = chatService.getById(addSubDto.getChatId());

        for (Long id : addSubDto.getUserIds()) {
            if (chat.getSubs().size() > 1 && chat.getChatType() == ChatType.PV)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            subscriptionService.createOrUpdate(new Subscription(null, userService.getById(id), chat));
        }
        return new ResponseEntity<>(chatService.getById(addSubDto.getChatId()), HttpStatus.OK);
    }
    @GetMapping("/chat-subs/{id}")
    public ResponseEntity<List<SubDto>> subsOfOneChat(@PathVariable Long id) {
        Chat chat = chatService.getById(id);
        List<SubDto> subDtoList = new ArrayList<>();
        for (Subscription sub:chat.getSubs()) {
            SubDto subDto = new SubDto();
            //contact
            subDto.setFirstName(sub.getUser().getFirstName());
            subDto.setLastName(sub.getUser().getUserName());
            subDto.setUserId(sub.getUser().getUserId());
            if (sub.getUser().getProfiles().size() != 0)
                subDto.setProfile(sub.getUser().getProfiles().get(sub.getUser().getProfiles().size() - 1).getMedia());
            subDtoList.add(subDto);
        }
        return new ResponseEntity<>(subDtoList , HttpStatus.OK);
    }
}
