package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;

public interface MessageService {

    Message getById(Long id);

    Iterable<Message> getAll();

    Iterable<Message> getByUser(User user);

    Iterable<Message> getByUser(Long userId) throws Exception;

    Iterable<Message> getByUser(String userName) throws Exception;

    Iterable<Message> getByChat(Chat chat);

    Iterable<Message> getByChat(Long chatId) throws Exception;

    Message createOrUpdate(Message message);

    void deleteById(Long id) throws Exception;

    void delete(Message message);

    void deleteByUser(User user);

    void deleteByUser(Long userId) throws Exception;

    void deleteByUser(String userName) throws Exception;

    void deleteByChat(Chat chat);

    void deleteByChat(Long chatId) throws Exception;
}
