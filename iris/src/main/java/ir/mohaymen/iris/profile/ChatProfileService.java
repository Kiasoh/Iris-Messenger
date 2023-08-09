package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;

import java.util.Optional;

public interface ChatProfileService {

    Iterable<ChatProfile> get();

    ChatProfile getById(Long id);

    ChatProfile create(ChatProfile chatProfile);

    void deleteById(Long id) throws Exception;
}
