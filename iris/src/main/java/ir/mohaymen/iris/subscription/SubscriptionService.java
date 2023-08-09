package ir.mohaymen.iris.subscription;

public interface SubscriptionService {

    Subscription getSubscriptionBySubscriptionId(Long subscriptionId);

    Iterable<Subscription> getAllSubscriptionByUserId(Long userId);

    Iterable<Subscription> getAllSubscriptionByChatId(Long chatId);

    Iterable<Subscription> getAll();

    Subscription createOrUpdate(Subscription subscription);

    void deleteById(Long subscriptionId);

    void deleteByUserId(Long userId);

    void deleteByChatId(Long chatId);
}
