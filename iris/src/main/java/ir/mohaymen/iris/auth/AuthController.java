package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthDto> login(
      @RequestBody @Valid LoginDto request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<String> refreshToken(@RequestBody TokenDto tokenDto) {
    return ResponseEntity.ok(authService.refreshToken(tokenDto));
  }

  @GetMapping("/send-activation-code")
  public String sendActivationCode(@RequestParam String phoneNumber) {
    return authService.sendActivationCode(phoneNumber);
  }


}
