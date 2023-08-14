package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SubscriptionSeeder implements Seeder {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void load() {
        if (subscriptionRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 500;
        final List<Subscription> subscriptions = new ArrayList<>();
        final Map<Long, Set<Long>> userToChatMap = new HashMap<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomSubscription(subscriptions, userToChatMap);
        subscriptionRepository.saveAll(subscriptions);
    }

    private void generateRandomSubscription(List<Subscription> subscriptionList, Map<Long, Set<Long>> userToChatMap) {
        long userId = faker.random().nextInt(1, 100);
        User user = new User();
        user.setUserId(userId);

        userToChatMap.computeIfAbsent(userId, k -> new HashSet<>());

        long chatId;
        do {
            chatId = faker.random().nextInt(1, 100);
        } while (userToChatMap.get(userId).contains(chatId));
        Chat chat = new Chat();
        chat.setChatId(chatId);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setChat(chat);

        subscriptionList.add(subscription);
        userToChatMap.get(userId).add(chatId);
    }
}
