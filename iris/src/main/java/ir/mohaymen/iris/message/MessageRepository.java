package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
                where m.messageId > :lastSeenMessageId and m.chat.chatId = :chatId
            """)
    long countUnSeenMessages(@Param("lastSeenMessageId") Long lastSeenMessageId,@Param("chatId") Long chatId);

    @Query(value = """
                select s
                From Subscription s
                where s.lastMessageSeenId > :messageId and s.chat = :chatId
            """)
    List<Subscription> usersSeen(@Param("messageId") Long messageId,@Param("chatId") Long chatId);

    @Query(value = """
        select m
        from Message m
        where m.chat.chatId = :chatId
        order by m.messageId desc 
""")
    List<Message> getLastMessageByChatId(@Param("chatId") Long chatId);
    Message findFirstByChatOrderByMessageIdDesc(Chat chat);
    @Query(value = """
        select s
        from Subscription s
        where s.lastMessageSeenId >= :messageId and s.chat.chatId = :chatId
""")
    List<Subscription> getSubSeen(@Param("messageId") Long messageId ,@Param("chatId") Long chatId);

}
