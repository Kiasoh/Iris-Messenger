package ir.mohaymen.iris.seeder;

import com.github.javafaker.DateAndTime;
import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaRepository;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MessageSeeder implements Seeder {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    @Override
    public void load() {
        if (!messageRepository.findAll().isEmpty()) return;

        final int NUMBER_OF_INSTANCES = 2000;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            Message message = generateRandomUser();
            if (message != null) messageRepository.save(message);
        }
    }

    private Message generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1,5}"));
        String text = fakeValuesService.regexify("(\\w|\\d|\\.| ){0,200}");

        long userId = Long.parseLong(fakeValuesService.regexify("\\d{1,2}"));
        User user = userRepository.findById(userId).orElse(null);

        long chatId = Long.parseLong(fakeValuesService.regexify("\\d{1,2}"));
        Chat chat = chatRepository.findById(chatId).orElse(null);

        long mediaId = Long.parseLong(fakeValuesService.regexify("\\d{1,2}"));
        Media media = mediaRepository.findById(mediaId).orElse(null);

        DateAndTime date = faker.date();
        Instant sendingTime = faker.date().past(200, TimeUnit.DAYS).toInstant();
        Instant editingTime = id % 6 == 0 ? date.future(200, TimeUnit.DAYS).toInstant() : null;

        if (user == null || chat == null) return null;
        if (text.isBlank() && media == null) return null;

        Message message = new Message();
        message.setMessageId(id);
        message.setText(text);
        message.setOriginChat(chat);
        message.setSender(user);
        message.setMedia(media);
        message.setSendAt(sendingTime);
        message.setEditedAt(editingTime);
        return message;
    }
}
