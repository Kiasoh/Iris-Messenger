package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
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
    private final SubscriptionService subscriptionService;

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
        return subscriptionService.isInChat(chat , user);
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
        List<Subscription> subs = subscriptionService.getAllSubscriptionByChatId(chat.getChatId());
        if (subs == null || subs.isEmpty())
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
//        if (chat.getChatType() != ChatType.PV || chat.getSubs().size() != 2)
//            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        if (subs.get(0).getUser().getUserId() == userId)
            return subs.get(1).getUser().getUserId();
        return subs.get(0).getUser().getUserId();
    }
}
