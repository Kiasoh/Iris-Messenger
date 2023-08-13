package ir.mohaymen.iris.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserSeeder userSeeder;
    private final ContactSeeder contactSeeder;
    private final ChatSeeder chatSeeder;
    private final SubscriptionSeeder subscriptionSeeder;
    private final MediaSeeder mediaSeeder;
    private final MessageSeeder messageSeeder;
    private final PinSeeder pinSeeder;
    private final UserProfileSeeder userProfileSeeder;
    private final ChatProfileSeeder chatProfileSeeder;

    @Override
    public void run(String... args) {
         userSeeder.load();
         contactSeeder.load();
         chatSeeder.load();
         subscriptionSeeder.load();
         mediaSeeder.load();
         messageSeeder.load();
         pinSeeder.load();
         userProfileSeeder.load();
         chatProfileSeeder.load();
    }
}
