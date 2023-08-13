package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaRepository;
import ir.mohaymen.iris.profile.UserProfile;
import ir.mohaymen.iris.profile.UserProfileRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
        final List<Long> mediaIds = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomUserProfile(userProfiles, mediaIds);
        userProfileRepository.saveAll(userProfiles);
    }

    private void generateRandomUserProfile(List<UserProfile> userProfiles, List<Long> mediaIds) {
        long userId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
        User user = new User();
        user.setUserId(userId);

        long mediaId;
        do {
            mediaId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|1[0-9][0-9]|200"));
        } while (mediaIds.contains(mediaId));
        Media media = new Media();
        media.setMediaId(mediaId);

        Instant sendingTime = faker.date().past(200, TimeUnit.DAYS).toInstant();

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setMedia(media);
        userProfile.setSetAt(sendingTime);

        mediaIds.add(mediaId);
        userProfiles.add(userProfile);
    }
}
