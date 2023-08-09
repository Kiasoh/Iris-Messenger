package ir.mohaymen.iris.profile;

import org.springframework.web.server.ResponseStatusException;

public interface UserProfileService {

    Iterable<UserProfile> get();

    UserProfile getById(Long id);

    UserProfile create(UserProfile userProfile);

    void deleteById(Long id);
}
