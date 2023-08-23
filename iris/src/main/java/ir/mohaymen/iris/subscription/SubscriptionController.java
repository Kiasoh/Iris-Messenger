package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.chat.GetChatDto;
import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.permission.Permission;
import ir.mohaymen.iris.permission.PermissionService;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
import ir.mohaymen.iris.profile.ProfileMapper;
import ir.mohaymen.iris.profile.UserProfile;
import ir.mohaymen.iris.profile.UserProfileService;
import ir.mohaymen.iris.search.chat.SearchChatDto;
import ir.mohaymen.iris.search.chat.SearchChatService;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/subs")
@RequiredArgsConstructor
public class SubscriptionController extends BaseController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ChatService chatService;
    private final ModelMapper modelMapper;
    private final ContactService contactService;
    private final PermissionService permissionService;
    SearchChatService searchChatService;
    private final MessageService messageService;
    private final UserProfileService userProfileService;

    @PostMapping("/add-user-to-chat")
    public ResponseEntity<GetChatDto> addToChat(@RequestBody @Valid AddSubDto addSubDto) {
        User user = getUserByToken();
        if (!permissionService.hasAccess(user.getUserId(), addSubDto.getChatId(), Permission.ADD_USER))
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);

        Chat chat = chatService.getById(addSubDto.getChatId());
        if (addSubDto.getUserIds().size() != 1 && chat.getChatType() == ChatType.PV)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Long lastMessageSeenId = 0L;
        Message message = messageService.getLastMessageByChatId(chat.getChatId());
        if (message != null)
            lastMessageSeenId = message.getMessageId();
        for (Long id : addSubDto.getUserIds()) {
            Subscription savedSub = subscriptionService.createOrUpdate(new Subscription(null, userService.getById(id),
                    chat, lastMessageSeenId, Permission.getDefaultPermissions(chat.getChatType())));
            if (!chat.getChatType().equals(ChatType.PV))
                searchChatService.index(new SearchChatDto(savedSub.getSubId(), savedSub.getUser().getUserId(), savedSub.getChat().getChatId(), savedSub.getChat().getTitle()));
        }
        GetChatDto getChatDto = modelMapper.map(chat, GetChatDto.class);
        getChatDto.setSubCount(subscriptionService.subscriptionCount(chat.getChatId()));
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }

    @PutMapping("/set-last-seen/{messageId}")
    public ResponseEntity<?> setLastSeen(@PathVariable Long messageId) {
        Message message = messageService.getById(messageId);
        subscriptionService.updateLastSeenMessage(message.getChat().getChatId() , getUserByToken().getUserId() , messageId);
        return ResponseEntity.ok("... YoU hAvE SeEn ThE tRuTh ...");
    }
    @DeleteMapping("/delete-sub/{subId}")
    public ResponseEntity<?> leaveGroupBySubId(@PathVariable Long subId) throws Exception {
        Subscription subscription = subscriptionService.getSubscriptionBySubscriptionId(subId);
        if (subscription.getChat().getChatType() == ChatType.PV) {
            chatService.deleteById(subscription.getChat().getChatId());
            return ResponseEntity.ok("PV deleted.");
        }
        subscriptionService.deleteById(subId);
        return ResponseEntity.ok("you have left the group!");
    }
    @DeleteMapping("/delete-sub/{chatId}")
    public ResponseEntity<?> leaveGroupByChatId(@PathVariable Long chatId) throws Exception {
        Chat chat = new Chat(); chat.setChatId(chatId); chat.setChatType(ChatType.PV); chat.setCreatedAt(Instant.now());
        Subscription subscription = subscriptionService.getSubscriptionByChatAndUser( chat, getUserByToken());
        if (subscription.getChat().getChatType() == ChatType.PV) {
            chatService.deleteById(subscription.getChat().getChatId());
            return ResponseEntity.ok("PV deleted.");
        }
        subscriptionService.deleteById(subscription.getSubId());
        return ResponseEntity.ok("you have left the group!");
    }
    @GetMapping("/chat-subs/{id}")
    public ResponseEntity<List<SubDto>> subsOfOneChat(@PathVariable Long id) {
        List<SubDto> subDtoList = new ArrayList<>();
        Chat chat = new Chat(); chat.setChatId(id);
        User user = getUserByToken();
        Subscription sub = subscriptionService.getSubscriptionByChatAndUser(chat , user);
        if ( sub.getPermissions() == null ||(chat.getChatType() == ChatType.CHANNEL && !sub.getPermissions().contains(Permission.ADMIN) ) )
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        for (Subscription subscription : subscriptionService.getAllSubscriptionByChatId(id)) {
            SubDto subDto = new SubDto();
            Nameable nameable = subscriptionService.setName(contactService.getContactByFirstUser(user),
                    subscription.getUser());
            subDto.setFirstName(nameable.getFirstName());
            subDto.setLastName(nameable.getLastName());
            subDto.setUserId(subscription.getUser().getUserId());
            subDto.setLastSeen(subscription.getUser().getLastSeen());
            subDto.setAdmin(Permission.isAdmin(subscription.getPermissions()));
            UserProfile profile = userProfileService.getLastUserProfile(subscription.getUser());
            if (profile != null)
                subDto.setProfile(ProfileMapper.mapToProfileDto(profile));
            subDtoList.add(subDto);
        }
        return new ResponseEntity<>(subDtoList, HttpStatus.OK);
    }
}
