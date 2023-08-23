package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ChatSeeder implements Seeder {

    private final ChatRepository chatRepository;

    static final int NUMBER_OF_INSTANCES = 50;
    private final List<Chat> chats = new ArrayList<>();
    private final Set<String> links = new HashSet<>();
    static final Map<Long, Set<Long>> ownerToChatMap = new HashMap<>();

    @Override
    public void load() {
        if (chatRepository.count() != 0)
            return;

        for (int i = 1; i <= NUMBER_OF_INSTANCES; i++)
            generateRandomChat(i);

        chats.sort(Comparator.comparing(Chat::getCreatedAt));
        chatRepository.saveAll(chats);
        clearReferences();
    }

    @Override
    public void clearReferences() {
        chats.clear();
        links.clear();
    }

    private void generateRandomChat(long chatIndex) {
        long id = faker.random().nextInt(1, 99999);
        String title = faker.book().title();
        ChatType chatType = ChatType.values()[faker.random().nextInt(1, ChatType.values().length - 1)];
        String bio = generateBio(id);
        boolean isPublic = id % 2 == 0;
        String link = (isPublic || id % 5 == 0) ? generateLink() : null;

        long ownerId = faker.random().nextInt(1, UserSeeder.NUMBER_OF_INSTANCES);
        User owner = new User();
        owner.setUserId(ownerId);

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
        chat.setOwner(owner);

        ownerToChatMap.computeIfAbsent(owner.getUserId(), k -> new HashSet<>());
        ownerToChatMap.get(owner.getUserId()).add(chatIndex);

        chats.add(chat);
    }

    private String generateBio(long seed) {
        if (seed % 3 == 0)
            return null;
        String bio = switch ((int) (seed % 5)) {
            case 0 -> faker.harryPotter().quote();
            case 1 -> faker.gameOfThrones().quote();
            case 2 -> faker.hobbit().quote();
            case 3 -> faker.dune().quote();
            default -> faker.rickAndMorty().quote();
        };
        return (bio.length() > 255) ? bio.substring(0, 255) : bio;
    }

    private String generateLink() {
        String link;
        do {
            link = faker.artist().name().replaceAll("\\s", "_").concat(faker.regexify("_\\d{2,5}_"))
                    .concat(faker.lordOfTheRings().character().replaceAll("\\s", "_"));
        } while (links.contains(link));
        links.add(link);
        return link;
    }
}
