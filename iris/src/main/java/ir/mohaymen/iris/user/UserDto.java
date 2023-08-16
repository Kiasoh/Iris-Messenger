package ir.mohaymen.iris.user;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userName;
    private String bio;
    private Instant lastSeen;
}
