package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;

public interface ChatProfileService {

    ChatProfile getById(Long id);

    Iterable<ChatProfile> getByChat(Chat chat);

    Iterable<ChatProfile> getByMedia(Media media);

    Iterable<ChatProfile> getAll();

    ChatProfile createOrUpdate(ChatProfile chatProfile);

    void deleteById(Long id);
}
