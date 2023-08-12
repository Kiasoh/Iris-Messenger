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

@Component
@RequiredArgsConstructor
public class UserProfileSeeder implements Seeder {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    @Override
    public void load() {
        final int NUMBER_OF_INSTANCES = 30;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            UserProfile userProfile = generateRandomUser();
            if (userProfile != null) userProfileRepository.save(userProfile);
        }
    }

    private UserProfile generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1-5}"));

        long userId = Long.parseLong(fakeValuesService.regexify("\\d{2}"));
        User user = userRepository.findById(userId).orElse(null);

        long mediaId = Long.parseLong(fakeValuesService.regexify("\\d{2}"));
        Media media = mediaRepository.findById(mediaId).orElse(null);

        Instant sendingTime = faker.date().birthday().toInstant();

        if (user == null || media == null) return null;

        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        userProfile.setUser(user);
        userProfile.setMedia(media);
        userProfile.setSetAt(sendingTime);
        return userProfile;
    }
}
