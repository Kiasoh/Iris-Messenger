package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostContactDto {
    private Long id;
    private User secondUser;
    private String firstName;
    private String lastName;
}
