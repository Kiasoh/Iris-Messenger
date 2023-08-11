package ir.mohaymen.iris.auth;

public interface AuthService {

    AuthTokensDto login(UserDto userDto);

    String refreshToken(AuthTokensDto authTokensDto);

    void sendActivationCode(String phoneNumber);

}
