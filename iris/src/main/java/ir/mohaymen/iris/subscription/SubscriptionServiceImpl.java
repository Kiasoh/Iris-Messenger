package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.permission.Permission;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.aggregations.metrics.InternalHDRPercentiles;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Override
    public Subscription getSubscriptionBySubscriptionId(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Chat PVExistance(Long userId1, Long userId2) {
        return subscriptionRepository.PVExists(userId1 , userId2);
    }

    @Override
    public Subscription createSavedMessage(User user) {
        Chat chat = new Chat();
        chat.setCreatedAt(Instant.now());
        chat.setTitle("Saved Message");
        chat.setPublic(false);
        chat.setChatType(ChatType.SAVED_MESSAGE);
        chat = chatRepository.save(chat);
        Subscription subscription = new Subscription();
        subscription.setLastMessageSeenId(0L);
        subscription.setPermissions(Permission.getDefaultPermissions(ChatType.PV));
        subscription.setUser(user);
        subscription.setChat(chat);
        return subscriptionRepository.save(subscription);
    }
    @Override
    public Nameable setName(List<Contact> contacts, User user) {
        for (Contact contact : contacts) {
            if (contact.getSecondUser() == user) {
                return contact;
            }
        }
        return user;
    }

    @Override
    public List<Subscription> getAllSubscriptionByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        return subscriptionRepository.findSubscriptionByUser(user);
    }

    @Override
    public boolean isInChat(Chat chat, User user) {
        return subscriptionRepository.userIsInChat(chat.getChatId() , user.getUserId());
    }

    @Override
    public Subscription getSubscriptionByChatAndUser(Chat chat, User user) {
        return subscriptionRepository.findSubscriptionByChatAndUser(chat , user);
    }

    @Override
    public void updateLastSeenMessage(Long chatId, Long userId, Long messageId) {
        subscriptionRepository.updateLastSeenMessage(chatId, userId, messageId);
    }

    @Override
    public Integer subscriptionCount(Long chatId) {
        return subscriptionRepository.subscriptionCount(chatId);
    }


    @Override
    public List<Subscription> getAllSubscriptionByChatId(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(EntityNotFoundException::new);
        return subscriptionRepository.findAllByChat(chat);
    }

    @Override
    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription createOrUpdate(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteById(Long subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        Iterable<Subscription> subscriptions = getAllSubscriptionByUserId(userId);
        subscriptionRepository.deleteAll(subscriptions);
    }

    @Override
    public void deleteByChatId(Long chatId) {
        Iterable<Subscription> subscriptions = getAllSubscriptionByChatId(chatId);
        subscriptionRepository.deleteAll(subscriptions);
    }

    public User getUserBySubscriptionId(Long subscriptionId) {
        Subscription subscription = getSubscriptionBySubscriptionId(subscriptionId);
        return subscription.getUser();
    }

    public Chat getChatBySubscriptionId(Long subscriptionId) {
        Subscription subscription = getSubscriptionBySubscriptionId(subscriptionId);
        return subscription.getChat();
    }
}
