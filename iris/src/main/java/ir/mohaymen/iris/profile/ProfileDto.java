package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileDto {

    private Long id;
    private Media media;
    private Instant setAt;
}
