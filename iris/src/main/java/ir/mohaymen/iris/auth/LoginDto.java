package ir.mohaymen.iris.auth;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
  @Pattern(regexp = "^09\\d{9}$")
  private String phoneNumber;
  private String activationCode;
}
