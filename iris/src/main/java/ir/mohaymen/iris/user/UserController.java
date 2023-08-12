package ir.mohaymen.iris.user;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        UserDto userDto = UserMapper.mapToUserDto(userService.getById(id));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody EditUserDto editUserDto, @PathVariable Long id){
        User user = userService.getById(id);
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());
        user.setUserName(editUserDto.getUserName());
        user.setBio(editUserDto.getBio());
        userService.createOrUpdate(user);
        return ResponseEntity.ok("success");
    }


}
