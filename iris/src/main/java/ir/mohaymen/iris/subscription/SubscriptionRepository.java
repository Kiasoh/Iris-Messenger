package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Iterable<Subscription> findSubscriptionByUser(User user);

    Iterable<Subscription> findAllByChat(Chat chat);
    Optional<Subscription> findByChatAndUser(Chat chat,User user);

}
