package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.utility.Nameable;

public interface SubscriptionService {

    Subscription getSubscriptionBySubscriptionId(Long subscriptionId);

    Nameable setName(Iterable<Contact> contacts, User user);

    Iterable<Subscription> getAllSubscriptionByUserId(Long userId);

    Iterable<Subscription> getAllSubscriptionByChatId(Long chatId);

    Iterable<Subscription> getAll();

    Subscription createOrUpdate(Subscription subscription);

    void deleteById(Long subscriptionId);

    void deleteByUserId(Long userId);

    void deleteByChatId(Long chatId);
}
