package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.media.Media;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private Media profile;
}