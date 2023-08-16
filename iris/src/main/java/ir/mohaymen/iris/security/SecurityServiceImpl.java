package ir.mohaymen.iris.security;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.profile.ChatProfileService;
import ir.mohaymen.iris.profile.UserProfileService;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserProfileService userProfileService;
    private final ChatProfileService chatProfileService;

    public boolean hasAccessToMedia(long userId, long mediaId) {
        Media media = Media.builder().mediaId(mediaId).build();
        User user = User.builder().userId(userId).build();

        //check if it is a message media:
        if (hasAccessToMessageMedia(media, user)) return true;

        //check it is a profile picture:
        if (userProfileService.isProfilePicture(media)) return true;

        //check if it is a chat picture:
        if (hasAccessToChatProfileMedia(media, user)) return true;

        return false;
    }

    private boolean hasAccessToChatProfileMedia(Media media, User user) {
        Iterable<ChatProfile> chatProfiles = chatProfileService.getByMedia(media);

        for (ChatProfile chatProfile : chatProfiles) {
            Chat chat = chatProfile.getChat();
            if (chat.isPublic()) return true;
            else if (chatService.isInChat(chat, user)) return true;
        }

        return false;
    }

    private boolean hasAccessToMessageMedia(Media media, User user) {
        Iterable<Message> messages = messageService.getByMedia(media);

        for (Message message : messages)
            if (chatService.isInChat(message.getOriginChat(), user)) return true;

        return false;
    }
}
