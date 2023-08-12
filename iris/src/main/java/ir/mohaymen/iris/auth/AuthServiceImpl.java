package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.code.ActivationCode;
import ir.mohaymen.iris.code.ActivationCodeRepository;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.token.Token;
import ir.mohaymen.iris.token.TokenRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import ir.mohaymen.iris.utility.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.MessageFormat;
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
    private Logger logger= LoggerFactory.getLogger(AuthService.class);;
    private AuthTokensDto register(String phoneNumber) {
        var user = User.builder()
                .firstName("کاربر")
                .phoneNumber(phoneNumber)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken();
        saveUserToken(savedUser, refreshToken);
        logger.info(MessageFormat.format("user with id:{0} phone number:{1} registered refresh_token:{2} jwt_token:{3} ",savedUser.getUserId(),phoneNumber,refreshToken.getToken(),jwtToken));
        return AuthTokensDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthTokensDto login(LoginDto loginDto) {

        if (!isActiveCodeValid(loginDto)) {
            logger.warn(MessageFormat.format("user entered {0} as activation code which is for sb else phone number:{1}", loginDto.getActivationCode(),loginDto.getPhoneNumber()));
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
        logger.info(MessageFormat.format("user phone number:{0} registered refresh_token:{1} jwt_token:{2} logined", loginDto.getPhoneNumber(),refreshToken,jwtToken));
        return AuthTokensDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private boolean isActiveCodeValid(LoginDto loginDto) {
        var activeCodeObj = activationCodeRepository.findByCode(loginDto.getActivationCode()).orElseThrow(()->{
            logger.warn(MessageFormat.format("user phone number:{0} entered {1} as activation code incorrectly", loginDto.getPhoneNumber(),loginDto.getActivationCode()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        });
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
        activationCode="12345";
        ActivationCode codeObj=ActivationCode.builder()
                .code(activationCode)
                .phoneNumber(phoneNumber)
                .build();
        activationCodeRepository.save(codeObj);
        smsService.sendSms(phoneNumber, "کد فعالسازی شما:" + activationCode);
    }

}
