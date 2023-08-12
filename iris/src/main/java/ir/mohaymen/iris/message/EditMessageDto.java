package ir.mohaymen.iris.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMessageDto {
    private Long messageId;
    private String text;
}
