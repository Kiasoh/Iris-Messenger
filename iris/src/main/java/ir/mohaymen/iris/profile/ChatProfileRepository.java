package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatProfileRepository extends JpaRepository<ChatProfile, Long> {

    Iterable<ChatProfile> findByChat(Chat chat);

}
