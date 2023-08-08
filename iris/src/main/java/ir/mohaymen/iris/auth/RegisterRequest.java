package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String phoneNumber;
  private String password;
  private Role role;
}
