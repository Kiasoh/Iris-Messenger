package ir.mohaymen.iris.profile;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProfileController {

    private final ChatProfileService chatProfileService;
    private final UserProfileService userProfileService;

    @GetMapping("/profile/users/{id}")
    public ResponseEntity<ProfileDto> getUserProfileById(@PathVariable Long id){
        ProfileDto profileDto = ProfileMapper.mapToProfileDto(userProfileService.getById(id));
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

//    @GetMapping("/profile/chats/{id}")
//    public ResponseEntity<ProfileDto> getChatProfileById(@PathVariable Long id){
//        ProfileDto profileDto = ProfileDto.mapToProfileDto()
//    }
}
