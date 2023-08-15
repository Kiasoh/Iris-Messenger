package ir.mohaymen.iris.pin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PinDto {
    @NotNull
    private Long messageId;
    @NotNull
    private Long chatId;
}
