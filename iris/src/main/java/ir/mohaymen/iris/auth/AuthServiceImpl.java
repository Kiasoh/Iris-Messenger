package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.code.ActivationCode;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.token.Token;
import ir.mohaymen.iris.token.TokenRepository;
import ir.mohaymen.iris.user.*;
import ir.mohaymen.iris.utility.CodeGenerator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SubscriptionService subscriptionService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final SMSService smsService;
    private final UserService userService;
    private final Cache cache;
    private Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, SubscriptionService subscriptionService, AuthenticationManager authenticationManager, ModelMapper mapper, SMSService smsService, UserService userService, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.subscriptionService = subscriptionService;
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
        this.smsService = smsService;
        this.userService = userService;
        this.cache= cacheManager.getCache("activationCodes");
    }

    private AuthDto register(String phoneNumber) {
        var user = User.builder()
                .firstName("کاربر")
                .phoneNumber(phoneNumber)
                .lastSeen(Instant.now())
                .build();
        var savedUser = userRepository.save(user);
        subscriptionService.createSavedMessage(savedUser);
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
                .isRegistered(true)
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
            userDto = result.getUser();
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
        cache.evictIfPresent(loginDto.getPhoneNumber());
        userService.setOnline(userDto.getUserId());
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
        ActivationCode activeCodeObj = cache.get(loginDto.getPhoneNumber(), ActivationCode.class);
        if (activeCodeObj == null) {
            logger.warn(MessageFormat.format("user phone number:{0} entered {1} as activation code while has no activation code", loginDto.getPhoneNumber(), loginDto.getActivationCode()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        boolean correct = activeCodeObj.getCode().equals(loginDto.getActivationCode());
        if (!correct) {
            logger.warn(MessageFormat.format("user phone number:{0} entered {1} as activation code incorrectly", loginDto.getPhoneNumber(), loginDto.getActivationCode()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return true;
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
        ActivationCode oldCode = cache.get(phoneNumber, ActivationCode.class);
        if (oldCode != null)
            return oldCode.getCode();
        String activationCode = CodeGenerator.generateActivationCode();
        ActivationCode codeObj = ActivationCode.builder()
                .phoneNumber(phoneNumber)
                .code(activationCode)
                .build();
        cache.put(phoneNumber, codeObj);
//        String message=
//                ;
        var utfMessage= """
Welcome to Iris Messenger.
Your Activation code:
                """.getBytes(StandardCharsets.UTF_8);
        var stringUtfMessage= new String(utfMessage, StandardCharsets.UTF_8);
        smsService.sendSms(phoneNumber, stringUtfMessage + activationCode);
        return activationCode;
    }

}
