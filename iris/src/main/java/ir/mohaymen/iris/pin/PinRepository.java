package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {

    Optional<Pin> findByMessage(Message message);

    Iterable<Pin> findByChat(Chat chat);

    void deleteByMessage(Message message);

    void deleteByChat(Chat chat);
}
