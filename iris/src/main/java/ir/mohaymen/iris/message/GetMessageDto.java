package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
public class GetMessageDto {
    private Long messageId;
    private Long userId;
    private String text;
    private Media media;
    private Instant sendAt;
    private Instant editedAt;
}
