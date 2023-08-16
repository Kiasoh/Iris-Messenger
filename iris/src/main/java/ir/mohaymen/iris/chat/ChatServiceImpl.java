package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Chat getById(Long id) {
        return chatRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Iterable<Chat> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public boolean isInChat(Chat chat, User user) {
        Set<Subscription> subs = chat.getSubs();
        return subs.stream().anyMatch(s -> Objects.equals(s.getUser().getUserId(), user.getUserId()));
    }

    @Override
    public Chat createOrUpdate(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public void deleteById(Long id) {
        chatRepository.deleteById(id);
    }

    public Chat getByLink(String link) {
        return chatRepository.findByLink(link).orElseThrow(EntityNotFoundException::new);
    }

    public Iterable<Chat> getByTitle(String title) {
        return chatRepository.findByTitle(title);
    }
}
