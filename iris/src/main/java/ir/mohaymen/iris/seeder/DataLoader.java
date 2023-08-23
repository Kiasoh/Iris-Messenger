package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.search.ConfigureAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserSeeder userSeeder;
    private final ContactSeeder contactSeeder;
    private final ChatSeeder chatSeeder;
    private final SubscriptionSeeder subscriptionSeeder;
    private final PVSeeder pvSeeder;
    private final MediaSeeder mediaSeeder;
    private final UserProfileSeeder userProfileSeeder;
    private final ChatProfileSeeder chatProfileSeeder;
    private final MessageSeeder messageSeeder;
    private final PinSeeder pinSeeder;

    @Override
    public void run(String... args) {
        userSeeder.load();
        contactSeeder.load();
        chatSeeder.load();
        subscriptionSeeder.load();
        pvSeeder.load();
        mediaSeeder.load();
        userProfileSeeder.load();
        chatProfileSeeder.load();
        messageSeeder.load();
        pinSeeder.load();
        clearReferences();
    }

    private void clearReferences() {
        ChatSeeder.ownerToChatMap.clear();
        MediaSeeder.mediaIds.clear();
    }
}
