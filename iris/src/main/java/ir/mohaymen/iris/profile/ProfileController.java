package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ChatProfileService chatProfileService;
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final ChatService chatService;

    @GetMapping("/users/{id}")
    public ResponseEntity<ProfileDto> getUserProfileById(@PathVariable Long id){
        ProfileDto profileDto = ProfileMapper.mapToProfileDto(userProfileService.getById(id));
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @GetMapping("/chats/{id}")
    public ResponseEntity<ProfileDto> getChatProfileById(@PathVariable Long id){
        ProfileDto profileDto = ProfileMapper.mapToProfileDto(chatProfileService.getById(id));
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<String> addUserProfile(@RequestPart("file") MultipartFile file, @PathVariable Long id){
        User user = userService.getById(id);
        Media media = Media.builder().fileMimeType(file.getContentType()).fileName(file.getOriginalFilename()).build();
        UserProfile userProfile = UserProfile.builder().user(user).setAt(Instant.now()).media(media).build();
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
