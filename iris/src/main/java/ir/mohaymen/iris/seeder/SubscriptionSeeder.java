package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.permission.Permission;
import ir.mohaymen.iris.permission.PermissionService;
import ir.mohaymen.iris.search.chat.SearchChatDto;
import ir.mohaymen.iris.search.chat.SearchChatService;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SubscriptionSeeder implements Seeder {

    private final SubscriptionRepository subscriptionRepository;
    private final ChatRepository chatRepository;

    static final int NUMBER_OF_INSTANCES = ChatSeeder.NUMBER_OF_INSTANCES * 4;
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final Map<Long, Set<Long>> userToChatMap = new HashMap<>();
    private final SearchChatService searchChatService;

    @Override
    public void load() {
        if (subscriptionRepository.count() != 0)
            return;

        addOwnersToSubscription();
        for (int i = 0; i < NUMBER_OF_INSTANCES - ChatSeeder.NUMBER_OF_INSTANCES; i++)
            generateRandomSubscription();

        var savedSubs = subscriptionRepository.saveAll(subscriptions);
        searchChatService
                .bulkIndex(savedSubs.stream()
                        .map(s -> new SearchChatDto(s.getSubId(), s.getUser().getUserId(),s.getChat().getChatId(), chatRepository.getTitle(s.getChat().getChatId())))
                        .toList());
        clearReferences();
    }

    @Override
    public void clearReferences() {
        subscriptions.clear();
        userToChatMap.clear();
    }

    private void addOwnersToSubscription() {
        for (Long ownerId : ChatSeeder.ownerToChatMap.keySet()) {
            Set<Long> chats = ChatSeeder.ownerToChatMap.get(ownerId);
            User owner = new User();
            owner.setUserId(ownerId);
            userToChatMap.computeIfAbsent(ownerId, k -> new HashSet<>());
            for (Long chatId : chats)
                createSubscription(owner, chatId);
        }
    }

    private void generateRandomSubscription() {
        long userId = faker.random().nextInt(1, UserSeeder.NUMBER_OF_INSTANCES);
        User user = new User();
        user.setUserId(userId);

        userToChatMap.computeIfAbsent(userId, k -> new HashSet<>());

        long chatId;
        do {
            chatId = faker.random().nextInt(1, ChatSeeder.NUMBER_OF_INSTANCES);
        } while (userToChatMap.get(userId).contains(chatId));

        createSubscription(user, chatId);
    }

    private void createSubscription(User user, long chatId) {
        Chat chat = new Chat();
        chat.setChatId(chatId);

        ChatType chatType = chatRepository.getChatType(chatId);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setChat(chat);
        subscription.setPermissions(Permission.getDefaultPermissions(chatType));

        subscriptions.add(subscription);
        userToChatMap.get(user.getUserId()).add(chatId);
    }
}
