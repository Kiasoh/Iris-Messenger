package ir.mohaymen.iris.auth;

public interface AuthService {

    AuthDto login(LoginDto loginDto);

    String refreshToken(AuthDto authDto);

    String sendActivationCode(String phoneNumber);

}
