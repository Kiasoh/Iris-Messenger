package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Iterable<Subscription> getAllSubscriptionByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        if (user == null) return null;
        return subscriptionRepository.findSubscriptionByUser(user);
    }
    @Override
    public Nameable setName(Iterable<Contact> contacts,User user) {
        for (Contact contact: contacts) {
            if (contact.getSecondUser() == user) {
                return contact;
            }
        }
        return user;
    }

    @Override
    public Iterable<Subscription> getAllSubscriptionByChatId(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(EntityNotFoundException::new);
        if (chat == null) return null;
        return subscriptionRepository.findAllByChat(chat);
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
        if (subscription == null) return null;
        return subscription.getUser();
    }

    public Chat getChatBySubscriptionId(Long subscriptionId) {
        Subscription subscription = getSubscriptionBySubscriptionId(subscriptionId);
        if (subscription == null) return null;
        return subscription.getChat();
    }
}
