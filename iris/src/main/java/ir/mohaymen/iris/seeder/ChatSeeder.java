package ir.mohaymen.iris.seeder;

import com.github.javafaker.DateAndTime;
import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatSeeder implements Seeder {

    private final ChatRepository chatRepository;

    static final int NUMBER_OF_INSTANCES = 100;
    private final List<Chat> chats = new ArrayList<>();

    @Override
    public void load() {
        if (chatRepository.count() != 0) return;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomChat();
        chatRepository.saveAll(chats);
    }

    private void generateRandomChat() {
        long id = Long.parseLong(faker.regexify("\\d{1,5}"));
        String title = faker.book().title();
        ChatType chatType = ChatType.values()[faker.random().nextInt(1, ChatType.values().length - 1)];
        String bio = id % 3 == 0 ? faker.regexify("(\\w|\\d| |_|,|\\.){5,50}") : null;
        boolean isPublic = id % 2 == 0;
        String link = (isPublic || id % 5 == 0) ? faker.regexify("\\w(\\w|\\d|_){2,10}") : null;
        DateAndTime date = faker.date();
        Date sendingTimeLowerBound = Date
                .from(LocalDateTime.now(ZoneId.of("GB")).minusDays(200).atZone(ZoneId.systemDefault()).toInstant());
        Date sendingTimeUpperBound = Date
                .from(LocalDateTime.now(ZoneId.of("GB")).minusDays(100).atZone(ZoneId.systemDefault()).toInstant());
        Instant createdTime = date.between(sendingTimeLowerBound, sendingTimeUpperBound).toInstant();
        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setLink(link);
        chat.setPublic(isPublic);
        chat.setChatType(chatType);
        chat.setCreatedAt(createdTime);
        chat.setBio(bio);

        chats.add(chat);
    }
}
