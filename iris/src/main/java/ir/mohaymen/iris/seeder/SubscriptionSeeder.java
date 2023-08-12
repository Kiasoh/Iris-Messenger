package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionSeeder implements Seeder {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Override
    public void load() {
        final int NUMBER_OF_INSTANCES = 200;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            Subscription subscription = generateRandomUser();
            if (subscription != null) subscriptionRepository.save(subscription);
        }
    }

    private Subscription generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1-5}"));

        long userId = Long.parseLong(fakeValuesService.regexify("\\d{2}"));
        User user = userRepository.findById(userId).orElse(null);

        long chatId = Long.parseLong(fakeValuesService.regexify("\\d{2}"));
        Chat chat = chatRepository.findById(chatId).orElse(null);

        if (user == null || chat == null) return null;

        Subscription subscription = new Subscription();
        subscription.setSubId(id);
        subscription.setUser(user);
        subscription.setChat(chat);

        chat.getSubs().add(subscription);

        return subscription;
    }
}
