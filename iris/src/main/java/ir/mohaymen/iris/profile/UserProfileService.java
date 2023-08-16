package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;

public interface UserProfileService {

    UserProfile getById(Long id);

    Iterable<UserProfile> getByUser(User user);

    Iterable<UserProfile> getAll();

    boolean isProfilePicture(Media media);

    UserProfile createOrUpdate(UserProfile userProfile);

    void deleteById(Long id);
}
