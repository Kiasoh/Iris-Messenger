package ir.mohaymen.iris.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<User> getById(List<Long> ids) {
        return userRepository.findAllById(ids);
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
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User createOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (getById(id) != null) userRepository.deleteById(id);
        else throw new Exception("No such user exists.");
    }

    @Override
    public void deleteByUserName(String userName) throws Exception {
        if (getByUserName(userName) != null) userRepository.deleteByUserName(userName);
        else throw new Exception("No such username exists.");
    }

    @Override
    public void deleteByPhoneNumber(String phoneNumber) throws Exception {
        if (getByPhoneNumber(phoneNumber) != null) userRepository.deleteByPhoneNumber(phoneNumber);
        else throw new Exception("No such phone number exists.");
    }
}
