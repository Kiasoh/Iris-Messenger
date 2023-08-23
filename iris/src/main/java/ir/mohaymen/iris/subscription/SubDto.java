package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.profile.ProfileDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class SubDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private ProfileDto profile;
    private Instant lastSeen;
}
