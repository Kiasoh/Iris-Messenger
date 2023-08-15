package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.subscription.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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
//    private Set<Subscription> subs;
    public List<ChatProfile> chatProfiles;
}
