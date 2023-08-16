package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatProfileRepository extends JpaRepository<ChatProfile, Long> {

    Iterable<ChatProfile> findByChat(Chat chat);

    List<ChatProfile> findAllByMedia(Media media);
}
