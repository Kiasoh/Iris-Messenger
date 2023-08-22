package ir.mohaymen.iris.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ChatSeederDto {
    private Long chatId;
    private Instant createdAt;
}
