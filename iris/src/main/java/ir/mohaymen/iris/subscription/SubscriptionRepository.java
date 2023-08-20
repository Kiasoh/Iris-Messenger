package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findSubscriptionByUser(User user);

    List<Subscription> findAllByChat(Chat chat);

    @Query("""
            select 1
            from Subscription sub
            where sub.chat.chatId=:chatId and sub.user.userId=:userId
            """)
    boolean userIsInChat(Long userId, Long chatId);
    @Query("""
    select sub.user.userId
    from Subscription sub
    where sub.chat.chatId = :chatId and sub.user.userId != :userId
""")
    Long getOtherPVUserId(Long chatId , Long userId);

}
