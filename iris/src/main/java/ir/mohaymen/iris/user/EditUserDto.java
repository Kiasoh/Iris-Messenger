package ir.mohaymen.iris.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
    @NotNull
    private Long userId;
    @NotNull
    private String firstName;
    private String lastName;
    private String userName;
    private String bio;
}
