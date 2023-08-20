package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findSubscriptionByUser(User user);

    List<Subscription> findAllByChat(Chat chat);
    @Query(value = """
        select count(s)
        from Subscription s 
        where s.chat.chatId = ?1
""")
    Integer subscriptionCount(Long chatId);
    @Query(value = """
        update Subscription s
        set s.lastMessageSeenId = ?3
        where s.chat.chatId = ?1 and s.user.userId = ?2
""")
    void updateLastSeenMessage(Long chatId , Long userId , Long messageId);
    @Query("""
            SELECT CASE WHEN COUNT(sub) > 0 THEN TRUE ELSE FALSE END
            from Subscription sub
            where sub.chat.chatId=:chatId and sub.user.userId=:userId
            """)
    boolean userIsInChat(Long userId,Long chatId);


    Optional<Subscription> findByChatAndUser(Chat chat, User user);

    @Query("""
                select sub.user.userId
                from Subscription sub
                where sub.chat.chatId = :chatId and sub.user.userId <> :userId
            """)
    Long getOtherPVUserId(Long chatId, Long userId);

    Subscription findSubscriptionByChatAndUser(Chat chat , User user);
}
