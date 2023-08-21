package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
        where s.chat.chatId = :chatId
""")
    Integer subscriptionCount(@Param("chatId") Long chatId);
    @Query(value = """
        update Subscription s
        set s.lastMessageSeenId = :messageId
        where s.chat.chatId = :chatId and s.user.userId = :userId
""")
    void updateLastSeenMessage(@Param("chatId") Long chatId ,@Param("userId") Long userId ,@Param("messageId") Long messageId);
    @Query("""
            SELECT CASE WHEN COUNT(sub) > 0 THEN TRUE ELSE FALSE END
            from Subscription sub
            where sub.chat.chatId=:chatId and sub.user.userId=:userId
            """)
    boolean userIsInChat(@Param("userId") Long userId,@Param("chatId") Long chatId);


    Optional<Subscription> findByChatAndUser(Chat chat, User user);

    @Query("""
                select sub.user.userId
                from Subscription sub
                where sub.chat.chatId = :chatId and sub.user.userId <> :userId
            """)
    Long getOtherPVUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);

    Subscription findSubscriptionByChatAndUser(Chat chat , User user);

    @Query("""
                SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
                from Subscription sub1 inner join Subscription sub2 on sub1.chat.chatId=sub2.chat.chatId
                where sub1.user.userId = :userId1 and sub2.user.userId = :userId2
            """)
    boolean PVExists(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

}
