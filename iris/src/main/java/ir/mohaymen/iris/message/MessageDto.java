package ir.mohaymen.iris.message;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private Long repliedMessageId;
    private String text;
    @NotNull
    private Long chatId;
}

