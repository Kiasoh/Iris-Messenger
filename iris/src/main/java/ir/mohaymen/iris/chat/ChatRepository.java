package ir.mohaymen.iris.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByLink(String Link);

    Iterable<Chat> findByTitle(String title);
}
