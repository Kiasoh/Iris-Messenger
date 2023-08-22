package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.profile.ChatProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ChatProfileSeeder implements Seeder {

    private final ChatProfileRepository chatProfileRepository;

    static final int NUMBER_OF_INSTANCES = 30;
    private final List<ChatProfile> chatProfiles = new ArrayList<>();
    private final Set<Long> mediaIds = new HashSet<>();

    @Override
    public void load() {
        if (chatProfileRepository.count() != 0) return;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomChatProfile();
        chatProfiles.sort(Comparator.comparing(ChatProfile::getSetAt));
        chatProfileRepository.saveAll(chatProfiles);
    }

    private void generateRandomChatProfile() {
        long chatId = faker.random().nextInt(1, ChatSeeder.NUMBER_OF_INSTANCES);
        Chat chat = new Chat();
        chat.setChatId(chatId);

        long mediaId;
        do {
            mediaId = faker.random().nextInt(1, MediaSeeder.NUMBER_OF_INSTANCES);
        } while (mediaIds.contains(mediaId));
        Media media = new Media();
        media.setMediaId(mediaId);

        Instant sendingTime = faker.date().past(100, TimeUnit.DAYS).toInstant();

        ChatProfile chatProfile = new ChatProfile();
        chatProfile.setChat(chat);
        chatProfile.setMedia(media);
        chatProfile.setSetAt(sendingTime);

        mediaIds.add(mediaId);
        chatProfiles.add(chatProfile);
    }
}
