package ir.mohaymen.iris.message;

import ir.mohaymen.iris.media.Media;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class GetMessageDto {
    private Long messageId;
    private Long repliedMessagePlacement;
    private Long userId;
    private String text;
    private Media media;
    @CreationTimestamp
    private Instant sendAt;
    private Instant editedAt;
    private boolean isSeen;
}
