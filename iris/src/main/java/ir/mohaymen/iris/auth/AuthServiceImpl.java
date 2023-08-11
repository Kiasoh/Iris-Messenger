package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.code.ActivationCode;
import ir.mohaymen.iris.code.ActivationCodeRepository;
import ir.mohaymen.iris.token.Token;
import ir.mohaymen.iris.token.TokenRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import ir.mohaymen.iris.utility.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CodeGenerator codeGenerator;
    private final ModelMapper mapper;
    private final SMSService smsService;
    private final ActivationCodeRepository activationCodeRepository;

    private AuthTokensDto register(String phoneNumber) {
        var user = User.builder()
                .phoneNumber(phoneNumber)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken();
        saveUserToken(savedUser, refreshToken);
        return AuthTokensDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthTokensDto login(LoginDto loginDto) {

        if (!isActiveCodeValid(loginDto)) {
            System.err.println("not valid:"+loginDto.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String jwtToken, refreshToken;
        if (!userRepository.existsByPhoneNumber(loginDto.getPhoneNumber())) {
            var result = register(loginDto.getPhoneNumber());
            jwtToken = result.getAccessToken();
            refreshToken = result.getRefreshToken();
        } else {
            var user = userRepository.findByPhoneNumber(loginDto.getPhoneNumber())
                    .orElseThrow();
            jwtToken = jwtService.generateToken(user);
            var refreshTokenObj = jwtService.generateRefreshToken();
            refreshToken = refreshTokenObj.getToken();
            saveUserToken(user, refreshTokenObj);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getPhoneNumber(), "password"));
        activationCodeRepository.deleteByPhoneNumber(loginDto.getPhoneNumber());
        return AuthTokensDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private boolean isActiveCodeValid(LoginDto loginDto) {
        var activeCodeObj = activationCodeRepository.findByCode(loginDto.getActivationCode()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return activeCodeObj.getPhoneNumber().equals(loginDto.getPhoneNumber());
    }

    private void saveUserToken(User user, Token token) {
        token.setUser(user);
        tokenRepository.save(token);
    }

    public String refreshToken(AuthTokensDto authTokensDto) {
        var refreshToken = authTokensDto.getRefreshToken();
        var userPhoneNumber = jwtService.extractUsername(refreshToken);
        if (userPhoneNumber != null) {
            var user = this.userRepository.findByPhoneNumber(userPhoneNumber)
                    .orElseThrow();
            var refreshTokenObj = tokenRepository.findByToken(refreshToken).orElseThrow();

            if (refreshTokenObj.getUser().getPhoneNumber().equals(userPhoneNumber) &&
                    refreshTokenObj.getExpiresAt().isBefore(Instant.now())) {
                return jwtService.generateToken(user);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void sendActivationCode(String phoneNumber) {
        String activationCode;
        activationCodeRepository.deleteByPhoneNumber(phoneNumber);
        do {
            activationCode = codeGenerator.generateActivationCode();
        } while (activationCodeRepository.findByCode(activationCode).isPresent());
        ActivationCode codeObj=ActivationCode.builder()
                .code(activationCode)
                .phoneNumber(phoneNumber)
                .build();
        activationCodeRepository.save(codeObj);
        smsService.sendSms(phoneNumber, "کد فعالسازی شما:\n" + activationCode);
    }

}
