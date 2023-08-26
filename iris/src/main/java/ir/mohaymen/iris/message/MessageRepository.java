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

    @Query(value = """
            select m.chat.chatId
            from Message m
            where m.messageId = :id
            """)
    Long findChatByMessageId(@Param("id") Long id);

    @Query(value = """
            select count (m)
            from Message m
            where m.messageId > ?1 and m.chat = ?2
""")
    Long messagePlacementInChat(Long messageId , Long chatId);
    List<Message> findBySender(User user);

    List<Message> findByChat(Chat chat);

    @Query(value = """
            select m
            from Message m
            where m.chat.chatId = :chatId and m.media != null and m.messageId > :messageId
            """)
    List<Message> findNextMedia(@Param("chatId") Long chatId , @Param("messageId") Long messageId);

    List<Message> findAllByMedia(Media media);

    Iterable<Message> findByRepliedMessage(Message message);

    void deleteBySender(User user);

    void deleteByChat(Chat chat);

    @Query(value = """
                select count(m)
                From Message m
                where m.messageId > :lastSeenMessageId and m.chat.chatId = :chatId
            """)
    long countUnSeenMessages(@Param("lastSeenMessageId") Long lastSeenMessageId, @Param("chatId") Long chatId);

    @Query(value = """
                select s
                From Subscription s
                where s.lastMessageSeenId >= :messageId and s.chat.chatId = :chatId
            """)
    List<Subscription> usersSeen(@Param("messageId") Long messageId, @Param("chatId") Long chatId);

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
    List<Subscription> getSubSeen(@Param("messageId") Long messageId, @Param("chatId") Long chatId);

    @Query("""
            select new ir.mohaymen.iris.message.GetForwardMessageDto(m.chat.chatId, m.text)
            from Message m
            where m.messageId=:messageId
            """)
    GetForwardMessageDto findForwardMessageByMessageId(@Param("messageId") Long messageId);

    @Query("""
            select m.media
            from Message m
            where m.messageId=:messageId
            """)
    Media findMediaByMessageId(@Param("messageId") Long messageId);
}
