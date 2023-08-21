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
import java.util.Comparator;
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
        chats.sort(Comparator.comparing(Chat::getCreatedAt));
        chatRepository.saveAll(chats);
    }

    private void generateRandomChat() {
        long id = faker.random().nextInt(1, 99999);
        String title = faker.book().title();
        ChatType chatType = ChatType.values()[faker.random().nextInt(1, ChatType.values().length - 1)];
        String bio = generateBio(id);
        boolean isPublic = id % 2 == 0;
        String link = (isPublic || id % 5 == 0)
                ? faker.artist().name().replaceAll("\\s", "_").concat(faker.regexify("(\\w|\\d){2,5}"))
                : null;

        Date sendingTimeLowerBound = Date
                .from(LocalDateTime.now(ZoneId.of("GB")).minusDays(200).atZone(ZoneId.systemDefault()).toInstant());
        Date sendingTimeUpperBound = Date
                .from(LocalDateTime.now(ZoneId.of("GB")).minusDays(150).atZone(ZoneId.systemDefault()).toInstant());
        Instant createdTime = faker.date().between(sendingTimeLowerBound, sendingTimeUpperBound).toInstant();

        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setLink(link);
        chat.setPublic(isPublic);
        chat.setChatType(chatType);
        chat.setCreatedAt(createdTime);
        chat.setBio(bio);

        chats.add(chat);
    }

    private String generateBio(long seed) {
        if (seed % 3 == 0) return null;
        return switch ((int) (seed % 5)) {
            case 0 -> faker.harryPotter().quote();
            case 1 -> faker.gameOfThrones().quote();
            case 2 -> faker.hobbit().quote();
            case 3 -> faker.dune().quote();
            default -> faker.rickAndMorty().quote();
        };
    }
}
