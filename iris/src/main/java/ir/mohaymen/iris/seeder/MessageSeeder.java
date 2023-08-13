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
import java.util.ArrayList;
import java.util.List;
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
        final List<Long> mediaIds = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomMessage(messages, mediaIds);
        messageRepository.saveAll(messages);
    }

    private void generateRandomMessage(List<Message> messageList, List<Long> mediaIdList) {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1,5}"));

        String text = generateRandomText(id);

        long userId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
        User user = new User();
        user.setUserId(userId);

        long chatId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
        Chat chat = new Chat();
        chat.setChatId(chatId);

        Media media = generateRandomMedia(id, text, mediaIdList);

        DateAndTime date = faker.date();
        Instant sendingTime = faker.date().past(200, TimeUnit.DAYS).toInstant();
        Instant editingTime = id % 6 == 0 ? date.future(200, TimeUnit.DAYS).toInstant() : null;

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
            default -> "";
        };
    }

    private Media generateRandomMedia(Long seed, String messageText, List<Long> mediaIdList) {
        Media media;

        media = new Media();
        if (seed % 5 == 0 || messageText.isBlank()) {
            long mediaId;
            do {
                mediaId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|1[0-9][0-9]|200"));
            } while (mediaIdList.contains(mediaId));
            media.setMediaId(mediaId);
            mediaIdList.add(mediaId);
        } else
            media = null;

        return media;
    }
}
