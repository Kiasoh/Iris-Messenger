package ir.mohaymen.iris.user;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getUserName(),
                user.getBio(),
                user.getLastSeen()
        );
    }
}
