package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.subscription.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ChatDto {
    private String title;
    private String bio;
    private String link;
    private boolean isPublic;
    private ChatType chatType;
    private ArrayList<Subscription> subs;
}
