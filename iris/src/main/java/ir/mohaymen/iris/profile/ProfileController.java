package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.file.FileService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@AllArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController extends BaseController {

    private final ChatProfileService chatProfileService;
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final ChatService chatService;
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final MediaService mediaService;

    @GetMapping("/users/{id}")
    public ResponseEntity<List<ProfileDto>> getUserProfileById(@PathVariable Long id){
        User user = userService.getById(id);

        List<ProfileDto> profiles = user.getProfiles().stream()
                .map(ProfileMapper::mapToProfileDto)
                .toList();

        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @GetMapping("/chats/{id}")
    public ResponseEntity<List<ProfileDto>> getChatProfileById(@PathVariable Long id){
        Chat chat = chatService.getById(id);

        List<ProfileDto> profiles = chat.getChatProfiles().stream()
                .map(profile -> ProfileMapper.mapToProfileDto(profile))
                .toList();

        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @RequestMapping(path = "/users", method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addUserProfile(@RequestPart("file") MultipartFile file) throws IOException {
        User user = userService.getById(getUserByToken().getUserId());
        logger.info(MessageFormat.format("user with phone number:{0} attempts to upload profile picture:{1}", user.getPhoneNumber(), file.getOriginalFilename()));
        Media media = fileService.saveFile(file.getOriginalFilename(), file);
        UserProfile userProfile = UserProfile.builder().user(user).setAt(Instant.now()).media(media).build();
        userProfileService.createOrUpdate(userProfile);
        return ResponseEntity.ok("User profile added");
    }
    @RequestMapping(path = "/chats/{id}", method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addChatProfile(@RequestPart("file") MultipartFile file,@PathVariable Long id) throws IOException {
        User user = getUserByToken();
        //TODO: check if user has permission
        Chat chat=chatService.getById(id);
        logger.info(MessageFormat.format("user with phone number:{0} attempts to upload profile picture:{1} for chat", user.getPhoneNumber(), file.getOriginalFilename()));
        Media media = fileService.saveFile(file.getOriginalFilename(), file);
        ChatProfile chatProfile = ChatProfile.builder().chat(chat).setAt(Instant.now()).media(media).build();
        chatProfileService.createOrUpdate(chatProfile);

        return ResponseEntity.ok("Chat profile added");
    }
}
