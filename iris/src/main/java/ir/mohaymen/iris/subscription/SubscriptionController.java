package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.chat.GetChatDto;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.persistence.EntityNotFoundException;
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
    private final ContactService contactService;

    @PostMapping("/add-user-to-chat")
    public ResponseEntity<GetChatDto> addToChat(@RequestBody @Valid AddSubDto addSubDto) {
        Chat chat = chatService.getById(addSubDto.getChatId());
        for (Long id : addSubDto.getUserIds()) {
            if (chat.getSubs().size() > 1 && chat.getChatType() == ChatType.PV)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            subscriptionService.createOrUpdate(new Subscription( null, userService.getById(id),
                    chat , chat.getMessages().get(chat.getMessages().size() - 1).getMessageId()));
        }
        chat = chatService.getById(addSubDto.getChatId());
        GetChatDto getChatDto = modelMapper.map( chat , GetChatDto.class);
        getChatDto.setSubCount(chat.getSubs().size());
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }
    @PutMapping("/set-last-seen/{chatId}/{messageId}")
    public ResponseEntity<?> setLastSeen(@PathVariable Long chatId , @PathVariable Long messageId) {
        Chat chat = chatService.getById(chatId);
        Subscription subscription = null;
        for (Subscription sub:chat.getSubs()) {
            if (sub.getUser().getUserId() == getUserByToken().getUserId()){
                subscription = sub;
                break;
            }
        }
        if (subscription == null)
            throw new EntityNotFoundException();
        subscription.setLastMessageSeenId(messageId);
        subscriptionService.createOrUpdate(subscription);
        return ResponseEntity.ok("... YoU hAvE SeEn ThE tRuTh ...");
    }
    @GetMapping("/chat-subs/{id}")
    public ResponseEntity<List<SubDto>> subsOfOneChat(@PathVariable Long id) {
        Chat chat = chatService.getById(id);
        List<SubDto> subDtoList = new ArrayList<>();
        for (Subscription sub:chat.getSubs()) {
            SubDto subDto = new SubDto();
            Nameable nameable = subscriptionService.setName( contactService.getContactByFirstUser(getUserByToken()) , sub.getUser());
            subDto.setFirstName(nameable.getFirstName());
            subDto.setLastName(nameable.getLastName());
            subDto.setUserId(sub.getUser().getUserId());
            if (sub.getUser().getProfiles().size() != 0)
                subDto.setProfile(sub.getUser().getProfiles().get(sub.getUser().getProfiles().size() - 1).getMedia());
            subDtoList.add(subDto);
        }
        return new ResponseEntity<>(subDtoList , HttpStatus.OK);
    }
}
