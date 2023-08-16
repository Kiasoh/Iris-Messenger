package ir.mohaymen.iris.security;

import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.profile.ChatProfileRepository;
import ir.mohaymen.iris.profile.UserProfileRepository;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService{
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final UserProfileRepository userProfileRepository;
    private final ChatProfileRepository chatProfileRepository;
    public boolean hasAccessToMedia(long userId, long mediaId) {
        Media media=Media.builder().mediaId(mediaId).build();
        User user= User.builder().userId(userId).build();
        //check if it is a message media:
        if (hasAccessToMessageMedia(media, user)) return true;
        //check it is a profile picture:
        if(userProfileRepository.existsByMedia(media)) return true;
        //check if it is a chat picture:
        if (hasAccessToChatProfileMedia(media, user)) return true;

        return false;
    }

    private boolean hasAccessToChatProfileMedia(Media media, User user) {
        var chatProfiles=chatProfileRepository.findAllByMedia(media);
        for (ChatProfile chatProfile : chatProfiles) {
            var chat=chatProfile.getChat();
            if (chat.isPublic())
                return true;
            else if(chatService.isInChat(chat, user))
                return true;
        }
        return false;
    }

    private boolean hasAccessToMessageMedia(Media media, User user) {
        var messages=messageRepository.findAllByMedia(media);
        for (Message message : messages) {
            if(chatService.isInChat(message.getOriginChat(), user))
                return true;
        }
        return false;
    }
}
