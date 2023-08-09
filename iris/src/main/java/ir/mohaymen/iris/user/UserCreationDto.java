package ir.mohaymen.iris.user;

import lombok.Data;

@Data
public class UserCreationDto {

    private String phoneNumber;
    private String firstName;
    private String lastName;
}
