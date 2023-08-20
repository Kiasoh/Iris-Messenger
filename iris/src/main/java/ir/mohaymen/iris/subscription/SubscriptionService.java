package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.utility.Nameable;

import java.util.List;

public interface SubscriptionService {

    Subscription getSubscriptionBySubscriptionId(Long subscriptionId);

    Nameable setName(List<Contact> contacts, User user);

    List<Subscription> getAllSubscriptionByUserId(Long userId);

    Subscription getSubscriptionByChatAndUser(Chat chat , User user);

    void updateLastSeenMessage(Long chatId , Long userId , Long messageId);

    Integer subscriptionCount(Long chatId);

    List<Subscription> getAllSubscriptionByChatId(Long chatId);

    List<Subscription> getAll();

    Subscription createOrUpdate(Subscription subscription);

    void deleteById(Long subscriptionId);

    void deleteByUserId(Long userId);

    void deleteByChatId(Long chatId);
}
