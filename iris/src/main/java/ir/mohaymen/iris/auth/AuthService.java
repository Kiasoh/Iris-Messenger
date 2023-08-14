package ir.mohaymen.iris.auth;

public interface AuthService {

    AuthDto login(LoginDto loginDto);

    String refreshToken(TokenDto tokenDto);

    String sendActivationCode(String phoneNumber);

}
