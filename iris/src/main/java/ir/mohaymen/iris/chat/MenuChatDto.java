package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.media.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class MenuChatDto {
    private Long chatId;
    private String title;
    private Long unSeenMessages;
    private String lastMessage;
    private Instant sentAt;
    private String userFirstName;
    private ChatType chatType;
    private Media media;
}
