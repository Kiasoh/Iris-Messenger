package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.code.ActivationCode;
import ir.mohaymen.iris.code.ActivationCodeRepository;
import ir.mohaymen.iris.token.Token;
import ir.mohaymen.iris.token.TokenRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserDto;
import ir.mohaymen.iris.user.UserMapper;
import ir.mohaymen.iris.user.UserRepository;
import ir.mohaymen.iris.utility.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private Logger logger = LoggerFactory.getLogger(AuthService.class);

    private AuthDto register(String phoneNumber) {
        var user = User.builder()
                .firstName("کاربر")
                .phoneNumber(phoneNumber)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken();
        saveUserToken(savedUser, refreshToken);
        logger.info(
                MessageFormat.format("user with id:{0} phone number:{1} registered refresh_token:{2} jwt_token:{3} ",
                        savedUser.getUserId(), phoneNumber, refreshToken.getToken(), jwtToken));
        return AuthDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .user(UserMapper.mapToUserDto(savedUser))
                .build();
    }

    public AuthDto login(LoginDto loginDto) {

        if (!isActiveCodeValid(loginDto)) {
            logger.warn(
                    MessageFormat.format("user entered {0} as activation code which is for sb else phone number:{1}",
                            loginDto.getActivationCode(), loginDto.getPhoneNumber()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        boolean isRegistered = false;
        String jwtToken, refreshToken;
        UserDto userDto;
        if (!userRepository.existsByPhoneNumber(loginDto.getPhoneNumber())) {
            var result = register(loginDto.getPhoneNumber());
            jwtToken = result.getAccessToken();
            refreshToken = result.getRefreshToken();
            isRegistered = true;
            userDto=result.getUser();
        } else {
            var user = userRepository.findByPhoneNumber(loginDto.getPhoneNumber())
                    .orElseThrow();
            jwtToken = jwtService.generateToken(user);
            var refreshTokenObj = jwtService.generateRefreshToken();
            refreshToken = refreshTokenObj.getToken();
            saveUserToken(user, refreshTokenObj);
            userDto = UserMapper.mapToUserDto(user);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getPhoneNumber(), "password"));
        deleteAllOldCodes(loginDto.getPhoneNumber());
        logger.info(MessageFormat.format("user phone number:{0} registered refresh_token:{1} jwt_token:{2} logined",
                loginDto.getPhoneNumber(), refreshToken, jwtToken));
        return AuthDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .isRegistered(isRegistered)
                .user(userDto)
                .build();
    }

    private boolean isActiveCodeValid(LoginDto loginDto) {
        var activeCodeObj = activationCodeRepository.findByCode(loginDto.getActivationCode()).orElseThrow(() -> {
            logger.warn(MessageFormat.format("user phone number:{0} entered {1} as activation code incorrectly",
                    loginDto.getPhoneNumber(), loginDto.getActivationCode()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        });
        return activeCodeObj.getPhoneNumber().equals(loginDto.getPhoneNumber());
    }

    private void saveUserToken(User user, Token token) {
        token.setUser(user);
        tokenRepository.save(token);
    }

    public String refreshToken(TokenDto tokenDto) {
        var refreshToken = tokenDto.getRefreshToken();
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

    public String sendActivationCode(String phoneNumber) {
        if (activationCodeRepository.findByPhoneNumber(phoneNumber).isPresent())
            return "retry later";
        String activationCode;
        do {
            activationCode = codeGenerator.generateActivationCode();
        } while (activationCodeRepository.findByCode(activationCode).isPresent());
        ActivationCode codeObj = ActivationCode.builder()
                .phoneNumber(phoneNumber)
                .code(activationCode)
                .build();
        activationCodeRepository.save(codeObj);
        smsService.sendSms(phoneNumber, "کد فعالسازی شما:" + activationCode);
        return activationCode;
    }

    private void deleteAllOldCodes(String phoneNumber) {
        var oldCodes = activationCodeRepository.findAllByPhoneNumber(phoneNumber);
        activationCodeRepository.deleteAll(oldCodes);
    }

}
