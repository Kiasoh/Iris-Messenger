package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.profile.UserProfile;
import ir.mohaymen.iris.profile.UserProfileRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserProfileSeeder implements Seeder {

    private final UserProfileRepository userProfileRepository;

    static final int NUMBER_OF_INSTANCES = 30;
    private final List<UserProfile> userProfiles = new ArrayList<>();
    private final Set<Long> mediaIds = new HashSet<>();

    @Override
    public void load() {
        if (userProfileRepository.count() != 0) return;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomUserProfile();
        userProfiles.sort(Comparator.comparing(UserProfile::getSetAt));
        userProfileRepository.saveAll(userProfiles);
    }

    private void generateRandomUserProfile() {
        long userId = faker.random().nextInt(1, UserSeeder.NUMBER_OF_INSTANCES);
        User user = new User();
        user.setUserId(userId);

        long mediaId;
        do {
            mediaId = faker.random().nextInt(1, MediaSeeder.NUMBER_OF_INSTANCES);
        } while (mediaIds.contains(mediaId));
        Media media = new Media();
        media.setMediaId(mediaId);

        Instant sendingTime = faker.date().past(100, TimeUnit.DAYS).toInstant();

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setMedia(media);
        userProfile.setSetAt(sendingTime);

        mediaIds.add(mediaId);
        userProfiles.add(userProfile);
    }
}
