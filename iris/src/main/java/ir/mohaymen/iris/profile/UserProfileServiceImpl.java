package ir.mohaymen.iris.profile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public Iterable<UserProfile> get() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserProfile getById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    @Override
    public UserProfile create(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public void deleteById(Long id) {
        userProfileRepository.deleteById(id);
    }
}
