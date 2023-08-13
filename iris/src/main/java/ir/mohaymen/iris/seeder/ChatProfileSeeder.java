package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaRepository;
import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.profile.ChatProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatProfileSeeder implements Seeder {

    private final ChatProfileRepository chatProfileRepository;
    private final ChatRepository chatRepository;
    private final MediaRepository mediaRepository;
    private final Set<Long> mediaIds = new HashSet<>();

    @Override
    public void load() {
        if (!chatProfileRepository.findAll().isEmpty()) return;

        final int NUMBER_OF_INSTANCES = 30;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            ChatProfile chatProfile = generateRandomUser();
            if (chatProfile != null) chatProfileRepository.save(chatProfile);
        }
    }

    private ChatProfile generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1,5}"));

        long chatId = Long.parseLong(fakeValuesService.regexify("\\d{1,2}"));
        Chat chat = chatRepository.findById(chatId).orElse(null);

        long mediaId = Long.parseLong(fakeValuesService.regexify("\\d{1,2}"));
        Media media = mediaRepository.findById(mediaId).orElse(null);

        Instant sendingTime = faker.date().birthday().toInstant();

        if (chat == null || media == null) return null;

        if (mediaIds.contains(mediaId)) return null;
        else mediaIds.add(mediaId);

        for (ChatProfile chatProfile : chatProfileRepository.findByChat(chat))
            if (chatProfile.getMedia().equals(media)) return null;

        ChatProfile chatProfile = new ChatProfile();
        chatProfile.setId(id);
        chatProfile.setChat(chat);
        chatProfile.setMedia(media);
        chatProfile.setSetAt(sendingTime);
        return chatProfile;
    }
}
