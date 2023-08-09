package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Iterable<UserProfile> findByUser(User user);
}
