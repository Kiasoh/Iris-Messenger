package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Chat getById(Long id) {
        return chatRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Chat> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public boolean isInChat(Chat chat, User user) {
        Set<Subscription> subs = chat.getSubs();
        return subs.stream().anyMatch(s -> s.getUser().getUserId() == user.getUserId());
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
        return chatRepository.findByLink(link).orElse(null);
    }

    public Iterable<Chat> getByTitle(String title) {
        return chatRepository.findByTitle(title);
    }
}
