package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Iterable<Message> findBySender(User user);

    Iterable<Message> findByOriginChat(Chat chat);

    void deleteBySender(User user);

    void deleteByOriginChat(Chat chat);
}
