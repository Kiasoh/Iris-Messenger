package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.profile.ChatProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ChatProfileSeeder implements Seeder {

    private final ChatProfileRepository chatProfileRepository;

    @Override
    public void load() {
        if (chatProfileRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 30;
        final List<ChatProfile> chatProfiles = new ArrayList<>();
        final List<Long> mediaIds = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomChatProfile(chatProfiles, mediaIds);
        chatProfileRepository.saveAll(chatProfiles);
    }

    private void generateRandomChatProfile(List<ChatProfile> chatProfileList, List<Long> mediaIdList) {

        long chatId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
        Chat chat=  new Chat();
        chat.setChatId(chatId);

        long mediaId;
        do {
            mediaId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|1[0-9][0-9]|200"));
        } while (mediaIdList.contains(mediaId));
        Media media = new Media();
        media.setMediaId(mediaId);

        Instant sendingTime = faker.date().past(200, TimeUnit.DAYS).toInstant();

        ChatProfile chatProfile = new ChatProfile();
        chatProfile.setChat(chat);
        chatProfile.setMedia(media);
        chatProfile.setSetAt(sendingTime);

        mediaIdList.add(mediaId);
        chatProfileList.add(chatProfile);
    }
}
