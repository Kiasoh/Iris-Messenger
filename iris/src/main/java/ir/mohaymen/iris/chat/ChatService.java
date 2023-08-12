package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.user.User;

public interface ChatService {
    boolean isInChat(Chat chat, User user);
    Chat getById(Long id);

    Iterable<Chat> getAll();

    Chat createOrUpdate(Chat chat);

    void deleteById(Long id) throws Exception;
}
