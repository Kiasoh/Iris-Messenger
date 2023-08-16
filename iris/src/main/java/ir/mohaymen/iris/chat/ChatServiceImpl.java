package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
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
    @Override
    public Chat getByLink(String link) {
        return chatRepository.findByLink(link).orElseThrow(EntityNotFoundException::new);
    }

    public Iterable<Chat> getByTitle(String title) {
        return chatRepository.findByTitle(title);
    }
    public Long helloFromTheOtherSide (Chat chat ,Long userId) {
        if (chat.getSubs() == null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        if (chat.getChatType() != ChatType.PV || chat.getSubs().size() != 2)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<Subscription> bothSide = (List<Subscription>) chat.getSubs();
        if (bothSide.get(0).getUser().getUserId() == userId)
            return bothSide.get(1).getUser().getUserId();
        return bothSide.get(0).getUser().getUserId();
    }
}
