package ir.mohaymen.iris.seeder;

import com.github.javafaker.DateAndTime;
import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MessageSeeder implements Seeder {

    private final MessageRepository messageRepository;
    private final SubscriptionRepository subscriptionRepository;

    private static final int NUMBER_OF_CHAT_MESSAGES = 2000;
    private static final int NUMBER_OF_PV_MESSAGES = PVSeeder.NUMBER_OF_INSTANCES * 2;
    static final int NUMBER_OF_INSTANCES = NUMBER_OF_CHAT_MESSAGES + NUMBER_OF_PV_MESSAGES;
    private final List<Message> messages = new ArrayList<>();
    private final Set<Long> mediaIds = new HashSet<>();

    @Override
    public void load() {
        if (messageRepository.count() != 0)
            return;

        for (int i = 0; i < NUMBER_OF_CHAT_MESSAGES; i++)
            generateRandomMessageForChat();
        for (int i = 0; i < NUMBER_OF_PV_MESSAGES; i++)
            generateMessageForPV(i + 1);
        messages.sort(Comparator.comparing(Message::getSendAt));
        messageRepository.saveAll(messages);
    }

    private void generateRandomMessageForChat() {
        long id = faker.random().nextInt(1, 99999);

        long subscriptionId = faker.random().nextInt(1, SubscriptionSeeder.NUMBER_OF_INSTANCES);
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(new Subscription());

        generateMessage(subscription, id);
    }

    private void generateMessageForPV(long pvId) {
        long id = faker.random().nextInt(1, 99999);

        Subscription subscription = subscriptionRepository.findById(SubscriptionSeeder.NUMBER_OF_INSTANCES + pvId).orElse(new Subscription());

        generateMessage(subscription, id);
    }

    private void generateMessage(Subscription subscription, long id) {
        User user = subscription.getUser();
        Chat chat = subscription.getChat();

        Media media = generateRandomMedia(id);

        String text = String.join(" ", faker.lorem().paragraphs((int) id % 5 + 1));

        DateAndTime date = faker.date();
        Date sendingTimeLowerBound = Date.from(chat.getCreatedAt());
        Date sendingTimeUpperBound = Date
                .from(LocalDateTime.now(ZoneId.of("GB")).minusDays(100).atZone(ZoneId.systemDefault()).toInstant());
        Instant sendingTime = date.between(sendingTimeLowerBound, sendingTimeUpperBound).toInstant();
        Instant editingTime = id % 6 == 0 ? date.past(100, TimeUnit.DAYS).toInstant() : null;

        Message message = new Message();
        message.setText(text);
        message.setChat(chat);
        message.setSender(user);
        message.setMedia(media);
        message.setSendAt(sendingTime);
        message.setEditedAt(editingTime);

        messages.add(message);
    }

    private Media generateRandomMedia(long seed) {
        Media media;

        media = new Media();
        if (seed % 7 == 2 || seed % 7 == 6) {
            long mediaId;
            do {
                mediaId = faker.random().nextInt(1, MediaSeeder.NUMBER_OF_INSTANCES);
            } while (mediaIds.contains(mediaId));
            media.setMediaId(mediaId);
            mediaIds.add(mediaId);
        } else
            media = null;

        return media;
    }
}
