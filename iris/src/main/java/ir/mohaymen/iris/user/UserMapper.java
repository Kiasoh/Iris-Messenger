package ir.mohaymen.iris.user;

public class UserMapper {

    public static UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getUserName(),
                user.getBio()
        );
        return userDto;
    }
}
