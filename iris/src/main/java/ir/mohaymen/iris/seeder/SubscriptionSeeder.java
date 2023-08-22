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

    static final int NUMBER_OF_INSTANCES = 500;
    static final List<Subscription> subscriptions = new ArrayList<>();
    static final Map<Long, Set<Long>> userToChatMap = new HashMap<>();

    @Override
    public void load() {
        if (subscriptionRepository.count() != 0) return;

        addOwnersToSubscription();
        System.out.println(1);
        for (int i = 0; i < NUMBER_OF_INSTANCES - ChatSeeder.NUMBER_OF_INSTANCES; i++)
            generateRandomSubscription();
        subscriptionRepository.saveAll(subscriptions);
    }

    private void addOwnersToSubscription() {
        Map<Long, Set<Long>> ownerToChatMap = ChatSeeder.ownerToChatMap;
        for (Long ownerId : ChatSeeder.ownerToChatMap.keySet()) {
            Set<Long> chats = ChatSeeder.ownerToChatMap.get(ownerId);
            User owner = new User();
            owner.setUserId(ownerId);
            for (Long chatId : chats) {
                Chat chat = new Chat();
                chat.setChatId(chatId);

                Subscription subscription = new Subscription();
                subscription.setUser(owner);
                subscription.setChat(chat);

                subscriptions.add(subscription);
                userToChatMap.computeIfAbsent(ownerId, k -> new HashSet<>());
                userToChatMap.get(ownerId).add(chatId);
            }
        }
    }

    private void generateRandomSubscription() {
        long userId = faker.random().nextInt(1, UserSeeder.NUMBER_OF_INSTANCES);
        User user = new User();
        user.setUserId(userId);

        userToChatMap.computeIfAbsent(userId, k -> new HashSet<>());

        long chatId;
        do {
            chatId = faker.random().nextInt(1, ChatSeeder.NUMBER_OF_INSTANCES);
        } while (userToChatMap.get(userId).contains(chatId));
        Chat chat = new Chat();
        chat.setChatId(chatId);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setChat(chat);

        subscriptions.add(subscription);
        userToChatMap.get(userId).add(chatId);
    }
}
