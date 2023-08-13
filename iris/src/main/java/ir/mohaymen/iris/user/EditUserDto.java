package ir.mohaymen.iris.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String bio;
}
