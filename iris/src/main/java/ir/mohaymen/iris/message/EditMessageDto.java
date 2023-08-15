package ir.mohaymen.iris.message;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMessageDto {
    @NotNull
    private Long messageId;
    private String text;
}
