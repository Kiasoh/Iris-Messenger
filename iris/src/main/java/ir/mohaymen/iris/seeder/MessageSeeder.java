package ir.mohaymen.iris.seeder;

import com.github.javafaker.DateAndTime;
import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatSeederDto;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
import ir.mohaymen.iris.search.message.SearchMessageDto;
import ir.mohaymen.iris.search.message.SearchMessageService;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
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
    private final UserRepository userRepository;
    private final SearchMessageService searchMessageService;
    private final ModelMapper modelMapper;

    private static final int NUMBER_OF_CHAT_MESSAGES = ChatSeeder.NUMBER_OF_INSTANCES * 10;
    private static final int NUMBER_OF_PV_MESSAGES = PVSeeder.NUMBER_OF_INSTANCES * 2;
    static final int NUMBER_OF_INSTANCES = NUMBER_OF_CHAT_MESSAGES + NUMBER_OF_PV_MESSAGES;
    private final List<Message> messages = new ArrayList<>();
    private final Map<Long, Instant> userIdToLastSeenMap = new HashMap<>();

    @Override
    public void load() {
        if (messageRepository.count() != 0) return;

        for (int i = 0; i < NUMBER_OF_CHAT_MESSAGES; i++)
            generateRandomMessageForChat();
        for (int i = 1; i <= NUMBER_OF_PV_MESSAGES; i++)
            generateMessageForPV(i);

        updateUsersLastSeen();
        messages.sort(Comparator.comparing(Message::getSendAt));
        List<Message> savedMessages = messageRepository.saveAll(messages);
        searchMessageService
                .bulkIndex(savedMessages.stream()
                        .map(message -> new SearchMessageDto(message.getMessageId(), message.getText()))
                        .toList());
        clearReferences();
    }

    @Override
    public void clearReferences() {
        messages.clear();
    }

    private void generateRandomMessageForChat() {
        long subscriptionId = faker.random().nextInt(1, SubscriptionSeeder.NUMBER_OF_INSTANCES);

        generateMessage(subscriptionId);
    }

    private void generateMessageForPV(long pvId) {
        generateMessage(SubscriptionSeeder.NUMBER_OF_INSTANCES + pvId);
    }

    private void generateMessage(long subscriptionId) {
        long id = faker.random().nextInt(1, 99999);

        Long userId = subscriptionRepository.findUserIdBySubscriptionId(subscriptionId);
        User user = User.builder().userId(userId).build();

        ChatSeederDto chatDto = subscriptionRepository.findChatBySubscriptionId(subscriptionId);
        Chat chat = new Chat();
        chat.setChatId(chatDto.getChatId());

        Media media = generateRandomMedia(id);

        String text = (media == null || id % 6 == 0)
                ? String.join(" ", faker.lorem().paragraphs((int) id % 5 + 1))
                : null;

        DateAndTime date = faker.date();
        Date sendingTimeLowerBound = Date.from(chatDto.getCreatedAt());
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

        Instant updateTime = (editingTime == null) ? sendingTime : editingTime;

        if (userIdToLastSeenMap.get(userId) == null)
            userIdToLastSeenMap.put(userId, updateTime);
        else if (userIdToLastSeenMap.get(userId).isBefore(updateTime))
            userIdToLastSeenMap.put(userId, updateTime);

        messages.add(message);
    }

    private Media generateRandomMedia(long seed) {
        Media media;

        if (MediaSeeder.NUMBER_OF_USED_MEDIAS >= MediaSeeder.NUMBER_OF_INSTANCES) return null;

        media = new Media();
        if (seed % 7 == 2 || seed % 7 == 6) {
            long mediaId;
            do {
                mediaId = faker.random().nextInt(1, MediaSeeder.NUMBER_OF_INSTANCES);
            } while (MediaSeeder.mediaIds.contains(mediaId));
            media.setMediaId(mediaId);
            MediaSeeder.mediaIds.add(mediaId);
            MediaSeeder.NUMBER_OF_USED_MEDIAS++;
        } else
            media = null;

        return media;
    }

    private void updateUsersLastSeen() {
        for (Long userId : userIdToLastSeenMap.keySet())
            userRepository.updateLastSeenById(userId, userIdToLastSeenMap.get(userId));
    }
}
