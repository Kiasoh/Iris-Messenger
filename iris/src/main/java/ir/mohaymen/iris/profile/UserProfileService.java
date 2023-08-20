package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

public interface UserProfileService {

    UserProfile getById(Long id);

    List<UserProfile> getByUser(User user);

    UserProfile getLastUserProfile(User user);

    Iterable<UserProfile> getAll();

    boolean isProfilePicture(Media media);

    UserProfile createOrUpdate(UserProfile userProfile);

    void deleteById(Long id);
}
