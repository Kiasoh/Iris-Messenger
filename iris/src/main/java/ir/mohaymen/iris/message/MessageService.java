package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;

public interface MessageService {

    Message getById(Long id);

    Iterable<Message> getAll();

    Iterable<Message> getByUser(User user);

    Iterable<Message> getByChat(Chat chat);

    Iterable<Message> getByMedia(Media media);

    Message createOrUpdate(Message message);

    void deleteById(Long id);

    void delete(Message message);

    void deleteByUser(User user);

    void deleteByChat(Chat chat);
}
