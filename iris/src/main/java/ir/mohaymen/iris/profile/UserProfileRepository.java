package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    List<UserProfile> findByUser(User user);

    boolean existsByMedia(Media media);
}
