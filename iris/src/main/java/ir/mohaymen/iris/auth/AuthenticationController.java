package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.user.UserCreationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationDto> register(
      @RequestBody UserCreationDto user) {
    return ResponseEntity.ok(service.register(user));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationDto> authenticate(
      @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(service.login(request));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<String> refreshToken(@RequestBody AuthenticationDto authenticationDto) {
    return ResponseEntity.ok(service.refreshToken(authenticationDto));
  }
}
