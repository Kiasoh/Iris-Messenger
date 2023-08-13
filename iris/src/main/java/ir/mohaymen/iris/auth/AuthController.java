package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.code.ActivationCode;
import ir.mohaymen.iris.code.ActivationCodeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthTokensDto> login(
      @RequestBody LoginDto request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<String> refreshToken(@RequestBody AuthTokensDto authTokensDto) {
    return ResponseEntity.ok(authService.refreshToken(authTokensDto));
  }

  @GetMapping("/send-activation-code")
  public String sendActivationCode(@RequestParam String phoneNumber) {
    return authService.sendActivationCode(phoneNumber);
  }


}
