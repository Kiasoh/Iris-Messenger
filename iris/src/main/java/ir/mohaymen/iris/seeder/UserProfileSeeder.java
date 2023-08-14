package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.profile.UserProfile;
import ir.mohaymen.iris.profile.UserProfileRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserProfileSeeder implements Seeder {

    private final UserProfileRepository userProfileRepository;

    @Override
    public void load() {
        if (userProfileRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 30;
        final List<UserProfile> userProfiles = new ArrayList<>();
        final Set<Long> mediaIds = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomUserProfile(userProfiles, mediaIds);
        userProfileRepository.saveAll(userProfiles);
    }

    private void generateRandomUserProfile(List<UserProfile> userProfileList, Set<Long> mediaIdList) {
        long userId = faker.random().nextInt(1, 100);
        User user = new User();
        user.setUserId(userId);

        long mediaId;
        do {
            mediaId = faker.random().nextInt(1, 1000);
        } while (mediaIdList.contains(mediaId));
        Media media = new Media();
        media.setMediaId(mediaId);

        Instant sendingTime = faker.date().past(200, TimeUnit.DAYS).toInstant();

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setMedia(media);
        userProfile.setSetAt(sendingTime);

        mediaIdList.add(mediaId);
        userProfileList.add(userProfile);
    }
}
