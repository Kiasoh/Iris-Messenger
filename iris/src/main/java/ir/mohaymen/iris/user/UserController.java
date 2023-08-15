package ir.mohaymen.iris.user;

import ir.mohaymen.iris.utility.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController extends BaseController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
//        userService.getAll().forEach(UserMapper::mapToUserDto);
        List<UserDto> result = userRepository.findAll().stream().map(UserMapper::mapToUserDto).toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        UserDto userDto = UserMapper.mapToUserDto(userService.getById(id));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody EditUserDto editUserDto){
        User user = getUserByToken();
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());
        user.setUserName(editUserDto.getUserName());
        user.setBio(editUserDto.getBio());
        userService.createOrUpdate(user);
        return ResponseEntity.ok("success");
    }


}
