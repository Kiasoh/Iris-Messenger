package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
import ir.mohaymen.iris.pin.Pin;
import ir.mohaymen.iris.pin.PinRepository;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PinSeeder implements Seeder {

    private final PinRepository pinRepository;
    private final MessageRepository messageRepository;
    private final SubscriptionRepository subscriptionRepository;

    static final int NUMBER_OF_INSTANCES = 40;
    private final List<Pin> pins = new ArrayList<>();
    private final Set<Long> messageIds = new HashSet<>();

    @Override
    public void load() {
        if (pinRepository.count() != 0) return;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomPin();
        pinRepository.saveAll(pins);
    }

    private void generateRandomPin() {
        long messageId;
        do {
            messageId = faker.random().nextInt(1, MessageSeeder.NUMBER_OF_INSTANCES);
        } while (messageIds.contains(messageId));
        Message message = messageRepository.findById(messageId).orElse(new Message());

        Chat chat = message.getOriginChat();

        List<Subscription> subscriptions = new ArrayList<>();
        subscriptionRepository.findAllByChat(chat).iterator().forEachRemaining(subscriptions::add);
        int subscriptionId = faker.random().nextInt(0, subscriptions.size() - 1);
        User user = subscriptions.get(subscriptionId).getUser();

        Pin pin = new Pin();
        pin.setMessage(message);
        pin.setUser(user);
        pin.setChat(chat);

        messageIds.add(messageId);
        pins.add(pin);
    }
}
