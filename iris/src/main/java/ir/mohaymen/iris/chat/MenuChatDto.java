package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.media.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class MenuChatDto {
    private Long chatId;
    private String title;
    private String lastMessage;
    private String userFirstName;
    private Media media;
}
