package ir.mohaymen.iris.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetForwardMessageDto {
    private final Long chatId;
    private final String text;
}
