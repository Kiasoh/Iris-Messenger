package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;

import java.util.List;

public interface MessageService {

    Message getById(Long id);

    long countUnSeenMessages(Long lastSeenMessageId, Long chatId);

    List<Subscription> getSubSeen(Long messageId , Long chatId);

    Message getLastMessageByChatId(Chat chat);

    Long getChatIdByMessageId(Long messageId);

    List<Subscription> usersSeen(Long messageId, Long chatId);

    Iterable<Message> getAll();

    Iterable<Message> getByUser(User user);

    List<Message> getByChat(Chat chat);

    Iterable<Message> getByMedia(Media media);

    Iterable<Message> getByReplyMessage(Message message);

    Message createOrUpdate(Message message);
    Message getLastMessageByChatId(Long chatId);

    void deleteById(Long id);

    void delete(Message message);

    void deleteByUser(User user);

    void deleteByChat(Chat chat);
}
