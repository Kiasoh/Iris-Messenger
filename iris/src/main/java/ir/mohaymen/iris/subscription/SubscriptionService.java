package ir.mohaymen.iris.subscription;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;

public interface SubscriptionService {

    Subscription getSubscriptionBySubscriptionId(Long subscriptionId);

    User getUserBySubscriptionId(Long subscriptionId);

    Chat getChatBySubscriptionId(Long subscriptionId);

    Iterable<Subscription> getAllSubscriptionByUserId(Long userId);

    Iterable<Subscription> getAllSubscriptionByChatId(Long chatId);

    Subscription create(Subscription subscription);

    void deleteById(Long subscriptionId) throws Exception;

    void deleteByUserId(Long userId) throws Exception;

    void deleteByChatId(Long chatId)throws Exception;
}
