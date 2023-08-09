package ir.mohaymen.iris.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByLink(String Link);

    Iterable<Chat> findByTitle(String title);
}
