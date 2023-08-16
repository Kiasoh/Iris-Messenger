package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.chat.GetChatDto;
import ir.mohaymen.iris.profile.UserProfile;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/subs")
@RequiredArgsConstructor
public class SubscriptionController extends BaseController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ChatService chatService;
    private final ModelMapper modelMapper;

    @PostMapping("/add-user-to-chat")
    public ResponseEntity<GetChatDto> addToChat(@RequestBody @Valid AddSubDto addSubDto) {

        Chat chat = chatService.getById(addSubDto.getChatId());

        if (addSubDto.getUserIds().size() != 1 && chat.getChatType() == ChatType.PV)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        for (Long id : addSubDto.getUserIds())
            subscriptionService.createOrUpdate(new Subscription(null, userService.getById(id), chat));

        GetChatDto getChatDto = modelMapper.map(chat, GetChatDto.class);
        getChatDto.setSubCount(chat.getSubs().size());
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }

    @GetMapping("/chat-subs/{id}")
    public ResponseEntity<List<SubDto>> subsOfOneChat(@PathVariable Long id) {
        List<SubDto> subDtoList = new ArrayList<>();
        for (Subscription subscription : subscriptionService.getAllSubscriptionByChatId(id)) {
            SubDto subDto = new SubDto();
            //contact
            subDto.setFirstName(subscription.getUser().getFirstName());
            subDto.setLastName(subscription.getUser().getUserName());
            subDto.setUserId(subscription.getUser().getUserId());
            List<UserProfile> profiles = subscription.getUser().getProfiles();
            if (!profiles.isEmpty())
                subDto.setProfile(profiles.get(profiles.size() - 1).getMedia());
            subDtoList.add(subDto);
        }
        return new ResponseEntity<>(subDtoList, HttpStatus.OK);
    }
}
