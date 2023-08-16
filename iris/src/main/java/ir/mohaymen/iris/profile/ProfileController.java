package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.file.FileService;
import ir.mohaymen.iris.media.Media;
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

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@AllArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController extends BaseController {

    private final ChatProfileService chatProfileService;
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @GetMapping("/users/{id}")
    public ResponseEntity<ProfileDto> getUserProfileById(@PathVariable Long id) {
        ProfileDto profileDto = ProfileMapper.mapToProfileDto(userProfileService.getById(id));
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @GetMapping("/chats/{id}")
    public ResponseEntity<ProfileDto> getChatProfileById(@PathVariable Long id) {
        ProfileDto profileDto = ProfileMapper.mapToProfileDto(chatProfileService.getById(id));
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @RequestMapping(path = "/users", method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addUserProfile(@RequestPart("file") MultipartFile file) throws IOException {
        User user = userService.getById(getUserByToken().getUserId());
        logger.info(MessageFormat.format("user with phone number:{0} attempts to upload profile picture:{1}", user.getPhoneNumber(), file.getOriginalFilename()));
        Long mediaId = fileService.saveFile(file.getOriginalFilename(), file);
        UserProfile userProfile = UserProfile.builder().user(user).setAt(Instant.now()).media(Media.builder().mediaId(mediaId).build()).build();
        userProfileService.createOrUpdate(userProfile);

        return ResponseEntity.ok("success");
    }

//    @PostMapping("/chats/{id}")
//    public ResponseEntity<String> addChatProfile(@RequestPart("file") MultipartFile file){
//
//    }

//    @GetMapping("/profile/chats/{id}")
//    public ResponseEntity<ProfileDto> getChatProfileById(@PathVariable Long id){
//        ProfileDto profileDto = ProfileDto.mapToProfileDto()
//    }
}
