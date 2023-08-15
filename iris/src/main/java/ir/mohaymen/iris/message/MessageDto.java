package ir.mohaymen.iris.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private String text;
    @NotNull
    private Long chatId;
    private String fileName;
    private String fileContentType;
    private String filePath;

}

