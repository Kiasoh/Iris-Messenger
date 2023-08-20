package ir.mohaymen.iris.message;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    @Nullable
    private Long repliedMessageId;
    private String text;
    @NotNull
    private Long chatId;
    private MultipartFile file;
}

