package ir.mohaymen.iris.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Iterable<User> getByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @Override
    public Iterable<User> getByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User createOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }

    @Override
    public void deleteByPhoneNumber(String phoneNumber) {
        userRepository.deleteByPhoneNumber(phoneNumber);
    }

    @Override
    public void setOnline(Long userId) {
        userRepository.updateLastSeenById(userId,Instant.now());
    }

    @Override
    public void setOnline(String phoneNumber) {
        userRepository.updateLastSeenByPhoneNumber(phoneNumber,Instant.now());
    }

    public Iterable<User> getById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }
}
