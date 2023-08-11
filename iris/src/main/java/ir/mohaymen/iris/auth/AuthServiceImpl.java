package ir.mohaymen.iris.auth;

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

  private AuthTokensDto register(String phoneNumber) {
    //TODO: check if activation code is correct from redis
    var user=User.builder()
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

  public AuthTokensDto login(UserDto userDto) {
    //TODO: check activation code from redis
    String jwtToken,refreshToken;
    if (!userRepository.existsByPhoneNumber(userDto.getPhoneNumber())){
      var result=register(userDto.getPhoneNumber());
      jwtToken=result.getAccessToken();
      refreshToken=result.getRefreshToken();
    }
    else{
      var user = userRepository.findByPhoneNumber(userDto.getPhoneNumber())
              .orElseThrow();
      jwtToken = jwtService.generateToken(user);
      var refreshTokenObj = jwtService.generateRefreshToken();
      refreshToken=refreshTokenObj.getToken();
      saveUserToken(user, refreshTokenObj);
    }
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userDto.getPhoneNumber(), "password"));

    return AuthTokensDto.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
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
      var refreshTokenObj=tokenRepository.findByToken(refreshToken).orElseThrow();

      if (refreshTokenObj.getUser().getPhoneNumber().equals(userPhoneNumber) &&
              refreshTokenObj.getExpiresAt().isBefore(Instant.now()) ) {
        return jwtService.generateToken(user);
      }
    }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }
  public void sendActivationCode(String phoneNumber){
    var activationCode=codeGenerator.generateActivationCode();
    //TODO: save activation code into redis for 2 minutes
    smsService.sendSms(phoneNumber, "کد فعالسازی شما:\n" + activationCode);
  }

}
