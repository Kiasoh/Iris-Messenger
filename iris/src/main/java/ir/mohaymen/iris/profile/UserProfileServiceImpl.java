package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Override
    public Iterable<UserProfile> getAll() {
        return userProfileRepository.findAll();
    }


    @Override
    public boolean isProfilePicture(Media media) {
        return userProfileRepository.existsByMedia(media);
    }

    @Override
    public UserProfile getById(Long id) {
        return userProfileRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<UserProfile> getByUser(User user) {
        return userProfileRepository.findByUser(user);
    }

    @Override
    public UserProfile getLastUserProfile(User user) {
        return userProfileRepository.findFirstByUserOrderByIdDesc(user);
    }

    @Override
    public UserProfile createOrUpdate(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public void deleteById(Long id) {
        userProfileRepository.deleteById(id);
    }

    public Iterable<UserProfile> getByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        return getByUser(user);
    }
}
