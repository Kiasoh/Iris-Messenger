package ir.mohaymen.iris.pin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPinDto {
    private Long messagePlacement;
    private String messageText;
}
