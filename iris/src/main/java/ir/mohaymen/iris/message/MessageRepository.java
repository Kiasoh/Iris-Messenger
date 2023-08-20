package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySender(User user);

    List<Message> findByChat(Chat chat);

    List<Message> findAllByMedia(Media media);

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

    @Query(value = """
        select m
        from Message m
        where m.chat.chatId = ?1
        order by m.messageId desc 
""")
    List<Message> getLastMessageByChatId(Long chatId);
    Message findFirstByChatOrderByMessageIdDesc(Chat chat);
    @Query(value = """
        select s
        from Subscription s
        where s.lastMessageSeenId >= ?1 and s.chat.chatId = ?2
""")
    List<Subscription> getSubSeen(Long messageId , Long chatId);

}
