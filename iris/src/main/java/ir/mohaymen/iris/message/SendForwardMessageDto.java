package ir.mohaymen.iris.message;

import ir.mohaymen.iris.media.Media;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
public class SendForwardMessageDto {

    @NotNull
    private Long messageId;

    @NotNull
    private Long chatId;

    @NotNull
    private Long userId;

    @NotNull
    private Long originMessageId;

    private String text;

    private Media media;

    @CreationTimestamp
    private Instant sendAt;
}
