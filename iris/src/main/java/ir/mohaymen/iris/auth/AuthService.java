package ir.mohaymen.iris.auth;

public interface AuthService {

    AuthTokensDto login(LoginDto loginDto);

    String refreshToken(AuthTokensDto authTokensDto);

    void sendActivationCode(String phoneNumber);

}
