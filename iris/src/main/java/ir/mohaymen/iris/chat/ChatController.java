package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
import ir.mohaymen.iris.profile.ProfileDto;
import ir.mohaymen.iris.profile.ProfileMapper;
import ir.mohaymen.iris.permission.Permission;
import ir.mohaymen.iris.permission.PermissionService;
import ir.mohaymen.iris.profile.*;
import ir.mohaymen.iris.search.chat.SearchChatDto;
import ir.mohaymen.iris.search.chat.SearchChatService;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController extends BaseController {

    private final ChatService chatService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ChatProfileService chatProfileService;
    private final MessageService messageService;
    private final PermissionService permissionService;
    private final ContactService contactService;
    private final UserProfileService userProfileService;
    private final SearchChatService searchChatService;
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @PutMapping("/edit-chat-info")
    public ResponseEntity<?> editChatInfo(@RequestBody EditChatDto editChatDto) {
        Chat chat = chatService.getById(editChatDto.getId());
        chat.setBio(editChatDto.getBio());
        chat.setTitle(editChatDto.getTitle());
        chat.setLink(editChatDto.getLink());
        if (chat.isPublic() && !editChatDto.isPublic())
            chat.setLink(UUID.randomUUID().toString());
        chat.setPublic(editChatDto.isPublic());
        chatService.createOrUpdate(chat);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-chat")
    @Transactional
    public ResponseEntity<GetChatDto> createChat(@RequestBody @Valid CreateChatDto createChatDto) {
        if (createChatDto.getChatType() == ChatType.PV) {
            if (createChatDto.getUserIds().size() != 1) {
                if (createChatDto.getUserIds().contains(getUserByToken().getUserId())){
                    logger.error("PV chat can't have owner in adding list");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                logger.error("PV chat must have one user id in user ids");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            Chat chat;
            if ((chat = subscriptionService.PVExistance(getUserByToken().getUserId() , createChatDto.getUserIds().stream().toList().get(0) )) != null) {
                logger.info("Found a already existing PV chat. returning the chat.");
                return getGetChatDtoResponseEntity(chat);
            }
        }
        Chat chat = modelMapper.map(createChatDto, Chat.class);

        logger.info(MessageFormat.format("user with phone number:{0} wants to create chat ",
                getUserByToken().getPhoneNumber()));
        if (!chat.getChatType().equals(ChatType.PV) && chat.getTitle().isBlank()) {
            logger.error("non-PV chat must have title");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        chat.setCreatedAt(Instant.now());
        chat.setOwner(getUserByToken());
        if (chat.getChatType() == ChatType.PV){
            chat.setPublic(false);
            chat.setOwner(null);
            chat.setLink(null);
        }
        chat = chatService.createOrUpdate(chat);
        Set<Permission> ownerPermissions = chat.getChatType() == ChatType.PV
                ? Permission.getDefaultPermissions(chat.getChatType())
                : Permission.getAdminPermissions();
        createInternalSub(chat, getUserByToken(), ownerPermissions);
        for (Long userId : createChatDto.getUserIds()) {
            try {
                createInternalSub(chat, User.builder().userId(userId).lastSeen(Instant.now()).build());
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        logger.info("chat is created");
        return getGetChatDtoResponseEntity(chat);
    }

    @GetMapping("/get-chat/{id}")
    public ResponseEntity<GetChatDto> getChat(@PathVariable Long id) {
        Chat chat = chatService.getById(id);
        if (!chat.isPublic() && !chatService.isInChat(chat, getUserByToken()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return getGetChatDtoResponseEntity(chat);
    }

    @GetMapping("/{link}")
    public ResponseEntity<GetChatDto> getChatByLink(@PathVariable String link) {
        if (link == null || link.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Chat chat = chatService.getByLink(link);
        if (!chat.isPublic() && !chatService.isInChat(chat, getUserByToken()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return getGetChatDtoResponseEntity(chat);
    }

    private ResponseEntity<GetChatDto> getGetChatDtoResponseEntity(Chat chat) {
        logger.info(MessageFormat.format("chat with id:{0} is fetched by user:{1}", chat.getChatId(),
                getUserByToken().getPhoneNumber()));
        GetChatDto getChatDto = modelMapper.map(chat, GetChatDto.class);
        List<ProfileDto> profileDtoList = new ArrayList<>();
        if (chat.getChatType() != ChatType.PV) {
            List<ChatProfile> chatProfileList = chatProfileService.getByChat(chat);
            if (!chatProfileList.isEmpty())
                chatProfileList.forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
        } else {
            User user = userService.getById(chatService.getOtherPVUser(chat, getUserByToken().getUserId()));
            List<UserProfile> userProfileList = userProfileService.getByUser(user);
            if (userProfileList != null || !userProfileList.isEmpty())
                userProfileList
                        .forEach(chatProfile -> profileDtoList.add(ProfileMapper.mapToProfileDto(chatProfile)));
        }
        getChatDto.setChatId(chat.getChatId());
        getChatDto.setProfileDtoList(profileDtoList);
        getChatDto.setSubCount(subscriptionService.subscriptionCount(chat.getChatId()));
        getChatDto.setOwnerId(chat.getOwner().getUserId());
        return new ResponseEntity<>(getChatDto, HttpStatus.OK);
    }

    public MenuChatDto createMenuChat(Subscription sub) {
        Chat chat = sub.getChat();
        MenuChatDto menuChatDto = modelMapper.map(chat, MenuChatDto.class);
        if (chat.getChatType() != ChatType.PV) {
            ChatProfile chatProfile = chatProfileService.getLastChatProfile(chat);
            if (chatProfile != null )
                menuChatDto.setMedia(chatProfile.getMedia());
        }
        else {
            User user = userService.getById(chatService.getOtherPVUser(chat, getUserByToken().getUserId()));
            Nameable nameable = subscriptionService.setName(contactService.getContactByFirstUser(getUserByToken()), user);
            menuChatDto.setTitle(nameable.fullName());
            UserProfile userProfile = userProfileService.getLastUserProfile(user);
            if (userProfile != null)
                menuChatDto.setMedia(userProfile.getMedia());
            menuChatDto.setUserFirstName(nameable.getFirstName());
        }
        Message message = messageService.getLastMessageByChatId(chat);
        if (message != null) {
            menuChatDto.setUnSeenMessages(messageService.countUnSeenMessages(sub.getLastMessageSeenId(), chat.getChatId()));
            menuChatDto.setLastMessage(message.getText());
            menuChatDto.setSentAt(message.getSendAt());
            User user = message.getSender();
            Nameable nameable = subscriptionService.setName(contactService.getContactByFirstUser(getUserByToken()), user);
            UserProfile userProfile = userProfileService.getLastUserProfile(user);
            if (userProfile != null)
                menuChatDto.setMedia(userProfile.getMedia());
            menuChatDto.setUserFirstName(nameable.getFirstName());
        } else {
            menuChatDto.setSentAt(chat.getCreatedAt());
        }
        return menuChatDto;
    }

    @GetMapping("/get-all-chats")
    public ResponseEntity<List<MenuChatDto>> getAllChats() {
        List<MenuChatDto> menuChatDtos = new ArrayList<>();
        for (Subscription sub : subscriptionService.getAllSubscriptionByUserId(getUserByToken().getUserId())) {
            menuChatDtos.add(createMenuChat(sub));
        }
        menuChatDtos.sort((o1, o2) -> {
            if (o1.getSentAt().isBefore(o2.getSentAt()))
                return 1;
            return -1;
        });
        return new ResponseEntity<>(menuChatDtos, HttpStatus.OK);
    }

    @DeleteMapping("delete-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable Long id) throws Exception {
        if (!permissionService.isOwner(getUserByToken().getUserId(), id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        chatService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private Subscription createInternalSub(Chat chat, User user) {
        return createInternalSub(chat, user, Permission.getDefaultPermissions(chat.getChatType()));
    }

    private Subscription createInternalSub(Chat chat, User user, Set<Permission> permissions) {
        Subscription sub = new Subscription();
        sub.setChat(chat);
        sub.setUser(user);
        sub.setPermissions(permissions);
        Subscription savedSub = subscriptionService.createOrUpdate(sub);
        searchChatService.index(new SearchChatDto(savedSub.getSubId(), savedSub.getUser().getUserId(), savedSub.getChat().getChatId(), savedSub.getChat().getTitle()));
        return savedSub;
    }

}
