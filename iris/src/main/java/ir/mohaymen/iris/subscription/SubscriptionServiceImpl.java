package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;
    private ChatRepository chatRepository;

    @Override
    public Subscription getSubscriptionBySubscriptionId(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).orElse(null);
    }

    @Override
    public User getUserBySubscriptionId(Long subscriptionId) {
        Subscription subscription = getSubscriptionBySubscriptionId(subscriptionId);
        if (subscription == null) return null;
        return subscription.getUser();
    }

    @Override
    public Chat getChatBySubscriptionId(Long subscriptionId) {
        Subscription subscription = getSubscriptionBySubscriptionId(subscriptionId);
        if (subscription == null) return null;
        return subscription.getChat();
    }

    @Override
    public Iterable<Subscription> getAllSubscriptionByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        return subscriptionRepository.findSubscriptionByUser(user);
    }

    @Override
    public Iterable<Subscription> getAllSubscriptionByChatId(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) return null;
        return subscriptionRepository.findSubscriptionByChat(chat);
    }

    @Override
    public Iterable<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription createOrUpdate(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteById(Long subscriptionId) throws Exception {
        if (getSubscriptionBySubscriptionId(subscriptionId) != null)
            subscriptionRepository.deleteById(subscriptionId);
        else throw new Exception("No such subscription exists.");
    }

    @Override
    public void deleteByUserId(Long userId) throws Exception {
        Iterable<Subscription> subscriptions = getAllSubscriptionByUserId(userId);
        if (subscriptions != null) subscriptionRepository.deleteAll(subscriptions);
        else throw new Exception("No such user exists.");
    }

    @Override
    public void deleteByChatId(Long chatId) throws Exception {
        Iterable<Subscription> subscriptions = getAllSubscriptionByChatId(chatId);
        if (subscriptions != null) subscriptionRepository.deleteAll(subscriptions);
        else throw new Exception("No such chat exists.");
    }
}
