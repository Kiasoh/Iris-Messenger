package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Iterable<Message> findBySender(User user);

    Iterable<Message> findByChat(Chat chat);

    Iterable<Message> findAllByMedia(Media media);

    Iterable<Message> findByRepliedMessage(Message message);

    void deleteBySender(User user);

    void deleteByChat(Chat chat);

    @Query(value = """
                select count(m)
                From Message m
                where m.messageId > ?1 and m.chat.chatId = ?2
            """)
    long countUnSeenMessages(Long lastSeenMessageId, Long chatId);

    @Query(value = """
                select s
                From Subscription s
                where s.lastMessageSeenId > ?1 and s.chat = ?2
            """)
    List<Subscription> usersSeen(Long messageId, Long chatId);
}
