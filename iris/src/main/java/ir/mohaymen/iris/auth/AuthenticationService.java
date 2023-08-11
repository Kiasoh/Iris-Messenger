package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.token.Token;
import ir.mohaymen.iris.token.TokenRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserCreationDto;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final ModelMapper mapper;

  public AuthenticationDto register(UserCreationDto userDto) {
    var user=User.builder()
            .firstName(userDto.getFirstName())
            .lastName(userDto.getLastName())
            .phoneNumber(userDto.getPhoneNumber())
            .build();
    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken();
    saveUserToken(savedUser, refreshToken);
    return AuthenticationDto.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken.getToken())
        .build();
  }

  public AuthenticationDto login(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getPhoneNumber(), "password"));
    var user = userRepository.findByPhoneNumber(request.getPhoneNumber())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken();
    saveUserToken(user, refreshToken);
    return AuthenticationDto.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken.getToken())
        .build();
  }

  private void saveUserToken(User user, Token token) {
    token.setUser(user);
    tokenRepository.save(token);
  }

  public String refreshToken(AuthenticationDto authenticationDto) {
    var refreshToken = authenticationDto.getRefreshToken();
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
}
