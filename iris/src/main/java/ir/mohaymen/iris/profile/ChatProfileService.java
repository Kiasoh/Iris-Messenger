package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;

import java.util.List;

public interface ChatProfileService {

    ChatProfile getById(Long id);

    List<ChatProfile> getByChat(Chat chat);

    Iterable<ChatProfile> getByMedia(Media media);

    Iterable<ChatProfile> getAll();

    ChatProfile createOrUpdate(ChatProfile chatProfile);

    void deleteById(Long id);
}
