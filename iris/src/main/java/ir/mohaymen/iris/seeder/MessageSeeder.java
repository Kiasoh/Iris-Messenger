package ir.mohaymen.iris.seeder;

import com.github.javafaker.DateAndTime;
import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
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

    @Override
    public void load() {
        if (messageRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 2000;
        final List<Message> messages = new ArrayList<>();
        final Set<Long> mediaIds = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomMessage(messages, mediaIds);
        messageRepository.saveAll(messages);
    }

    private void generateRandomMessage(List<Message> messageList, Set<Long> mediaIdList) {
        long id = Long.parseLong(faker.regexify("\\d{1,5}"));

        long userId = faker.random().nextInt(1, 100);
        User user = new User();
        user.setUserId(userId);

        long chatId = faker.random().nextInt(1, 100);
        Chat chat = new Chat();
        chat.setChatId(chatId);

        Media media = generateRandomMedia(id, mediaIdList);

        String text = generateRandomText(id);

        DateAndTime date = faker.date();
        Date sendingTimeLowerBound = Date.from(LocalDateTime.now(ZoneId.of("GB")).minusDays(200).atZone(ZoneId.systemDefault()).toInstant());
        Date sendingTimeUpperBound = Date.from(LocalDateTime.now(ZoneId.of("GB")).minusDays(100).atZone(ZoneId.systemDefault()).toInstant());
        Instant sendingTime = date.between(sendingTimeLowerBound, sendingTimeUpperBound).toInstant();
        Instant editingTime = id % 6 == 0 ? date.past(100, TimeUnit.DAYS).toInstant() : null;

        Message message = new Message();
        message.setText(text);
        message.setOriginChat(chat);
        message.setSender(user);
        message.setMedia(media);
        message.setSendAt(sendingTime);
        message.setEditedAt(editingTime);

        messageList.add(message);
    }

    private String generateRandomText(Long seed) {
        return switch ((int) (seed % 7)) {
            case 0 -> faker.harryPotter().quote();
            case 1 -> faker.howIMetYourMother().quote();
            case 2 -> faker.gameOfThrones().quote();
            case 3 -> faker.hobbit().quote();
            case 4 -> faker.dune().quote();
            case 5 -> faker.rickAndMorty().quote();
            default -> null;
        };
    }

    private Media generateRandomMedia(Long seed, Set<Long> mediaIdList) {
        Media media;

        media = new Media();
        if (seed % 7 == 2 || seed % 7 == 6) {
            long mediaId;
            do {
                mediaId = faker.random().nextInt(1, 1000);
            } while (mediaIdList.contains(mediaId));
            media.setMediaId(mediaId);
            mediaIdList.add(mediaId);
        } else
            media = null;

        return media;
    }
}
