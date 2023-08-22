package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
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
    private final SubscriptionRepository subscriptionRepository;

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
        return subscriptionRepository.userIsInChat(user.getUserId(), chat.getChatId());
    }

    @Override
    public boolean isInChat(Long chatId, Long userId){
        return subscriptionRepository.userIsInChat(userId, chatId);
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
    public Long getOtherPVUser(Chat chat , Long userId) {
        if (!chat.getChatType().equals(ChatType.PV))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        return subscriptionRepository.getOtherPVUserId(chat.getChatId(),userId);
    }
}
