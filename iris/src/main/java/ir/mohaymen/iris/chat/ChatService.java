package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.user.User;

public interface ChatService {

    Chat getById(Long id);

    Chat getByLink(String link);

    Iterable<Chat> getAll();

    boolean isInChat(Chat chat, User user);

    Chat createOrUpdate(Chat chat);

    void deleteById(Long id) throws Exception;
    Long getOtherPVUser(Chat chat , Long userId);
}
