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

    private final MessageService messageService;
    private final UserProfileService userProfileService;

    @PostMapping("/add-user-to-chat")
    public ResponseEntity<GetChatDto> addToChat(@RequestBody @Valid AddSubDto addSubDto) {
        var user = getUserByToken();
        if (!permissionService.hasAccess(user.getUserId(), addSubDto.getChatId(), Permission.ADD_USER))
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);

        Chat chat = chatService.getById(addSubDto.getChatId());
        if (addSubDto.getUserIds().size() != 1 && chat.getChatType() == ChatType.PV)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Long lastMessageSeenId = 0L;
        Message message = messageService.getLastMessageByChatId(chat.getChatId());
        if (message != null)
            lastMessageSeenId = message.getMessageId();
        for (Long id : addSubDto.getUserIds())
            subscriptionService.createOrUpdate(new Subscription(null, userService.getById(id),
                    chat, lastMessageSeenId, Permission.getDefaultPermissions(chat.getChatType())));
        GetChatDto getChatDto = modelMapper.map(chat, GetChatDto.class);
        getChatDto.setSubCount(subscriptionService.subscriptionCount(chat.getChatId()));
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }

    @PutMapping("/set-last-seen/{chatId}/{messageId}")
    public ResponseEntity<?> setLastSeen(@PathVariable Long chatId, @PathVariable Long messageId) {
        subscriptionService.updateLastSeenMessage(chatId , getUserByToken().getUserId() , messageId);
        return ResponseEntity.ok("... YoU hAvE SeEn ThE tRuTh ...");
    }

    @GetMapping("/chat-subs/{id}")
    public ResponseEntity<List<SubDto>> subsOfOneChat(@PathVariable Long id) {
        List<SubDto> subDtoList = new ArrayList<>();
        for (Subscription subscription : subscriptionService.getAllSubscriptionByChatId(id)) {
            SubDto subDto = new SubDto();
            // contact
            Nameable nameable = subscriptionService.setName(contactService.getContactByFirstUser(getUserByToken()),
                    subscription.getUser());
            subDto.setFirstName(nameable.getFirstName());
            subDto.setLastName(nameable.getLastName());
            subDto.setUserId(subscription.getUser().getUserId());
            UserProfile profile = userProfileService.getLastUserProfile(subscription.getUser());
            if (profile != null)
                subDto.setProfile(ProfileMapper.mapToProfileDto(profile));
            subDtoList.add(subDto);
        }
        return new ResponseEntity<>(subDtoList, HttpStatus.OK);
    }
}
