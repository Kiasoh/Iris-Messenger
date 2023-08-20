package ir.mohaymen.iris.user;

import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.utility.BaseController;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    @GetMapping("/by-phoneNumber/{phone}")
    public ResponseEntity<Long> getUserIdByPhoneNumber (@PathVariable String phoneNumber) {
        return new ResponseEntity<>(userService.getByPhoneNumber(phoneNumber).getUserId() , HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserDto userDto = UserMapper.mapToUserDto(user);
        Nameable nameable = subscriptionService.setName(getUserByToken().getContacts(), user);
        userDto.setFirstName(nameable.getFirstName());
        userDto.setLastName(nameable.getFirstName());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @GetMapping("/get-current-user")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = UserMapper.mapToUserDto(getUserByToken());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody @Valid EditUserDto editUserDto) {
        User user = getUserByToken();
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());
        user.setUserName(editUserDto.getUserName());
        user.setBio(editUserDto.getBio());
        userService.createOrUpdate(user);
        return ResponseEntity.ok("success");
    }

}
