package ir.mohaymen.iris.message;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
public class MessageDto {
    private String text;

    private Long chatId;

    private Long userId;

    private String fileName;

    private String fileContentType;

    private String filePath;

}

