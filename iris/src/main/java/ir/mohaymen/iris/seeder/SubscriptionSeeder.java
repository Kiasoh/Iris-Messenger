package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatType;
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
    private final ChatRepository chatRepository;

    static final int NUMBER_OF_INSTANCES = 500;

    @Override
    public void load() {
        if (subscriptionRepository.count() != 0) return;

        final List<Subscription> subscriptions = new ArrayList<>();
        final Map<Long, Set<Long>> userToChatMap = new HashMap<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomSubscription(subscriptions, userToChatMap);
        subscriptionRepository.saveAll(subscriptions);
    }

    private void generateRandomSubscription(List<Subscription> subscriptionList, Map<Long, Set<Long>> userToChatMap) {
        long userId = faker.random().nextInt(1, UserSeeder.NUMBER_OF_INSTANCES);
        User user = new User();
        user.setUserId(userId);

        userToChatMap.computeIfAbsent(userId, k -> new HashSet<>());

        long chatId;
        do {
            chatId = faker.random().nextInt(1, ChatSeeder.NUMBER_OF_INSTANCES);
        } while (userToChatMap.get(userId).contains(chatId) || chatIsFull(chatId));
        Chat chat = new Chat();
        chat.setChatId(chatId);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setChat(chat);

        subscriptionList.add(subscription);
        userToChatMap.get(userId).add(chatId);
    }

    private boolean chatIsFull(long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(new Chat());
        return chat.getChatType().equals(ChatType.PV) && chat.getSubs().size() == 2;
    }
}
