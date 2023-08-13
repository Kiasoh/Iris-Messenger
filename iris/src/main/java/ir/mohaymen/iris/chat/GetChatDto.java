package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.profile.ChatProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GetChatDto {
    private Long chatId;
    private String title;
    private String bio;
    private String link;
    private boolean isPublic;
    private ChatType chatType;
    public int subCount;
    public List<ChatProfile> chatProfiles;
}
