package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostContactDto {
    private Long id;
    private User secondUser;
    private String firstName;
    private String lastName;
}
