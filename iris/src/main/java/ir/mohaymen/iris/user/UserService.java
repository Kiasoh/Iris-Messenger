package ir.mohaymen.iris.user;

import java.util.List;

public interface UserService {

    User getById(Long id);

    Iterable<User> getByFirstName(String firstName);

    Iterable<User> getByLastName(String lastName);

    User getByPhoneNumber(String phoneNumber);

    User getByUserName(String userName);

    List<User> getAll();

    User createOrUpdate(User user);

    void deleteById(Long id) throws Exception;

    void deleteByUserName(String userName);

    void deleteByPhoneNumber(String phoneNumber);
    void setOnline(Long userId);
    void setOnline(String phoneNumber);
}
