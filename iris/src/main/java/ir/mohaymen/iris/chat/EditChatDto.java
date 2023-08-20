package ir.mohaymen.iris.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
public class EditChatDto {
    private Long id;
    private String title;
    private String bio;
    private String link;
    private boolean isPublic;
}
