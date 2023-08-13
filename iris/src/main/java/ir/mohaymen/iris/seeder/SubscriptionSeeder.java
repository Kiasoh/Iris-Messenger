package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SubscriptionSeeder implements Seeder {

    private final SubscriptionRepository subscriptionRepository;
    
    @Override
    public void load() {
        if (subscriptionRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 500;
        List<Subscription> subscriptions = new ArrayList<>();
        Map<Long, List<Long>> userToChatMap = new HashMap<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomSubscription(subscriptions, userToChatMap);
        subscriptionRepository.saveAll(subscriptions);
    }

    private void generateRandomSubscription(List<Subscription> subscriptionList, Map<Long, List<Long>> userToChatMap) {
        long userId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
        User user = new User();
        user.setUserId(userId);

        userToChatMap.computeIfAbsent(userId, k -> new ArrayList<>());

        long chatId;
        do {
            chatId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
        } while (userToChatMap.get(userId).contains(chatId));
        Chat chat = new Chat();
        chat.setChatId(chatId);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setChat(chat);

        chat.getSubs().add(subscription);

        subscriptionList.add(subscription);
        userToChatMap.get(userId).add(chatId);
    }
}
