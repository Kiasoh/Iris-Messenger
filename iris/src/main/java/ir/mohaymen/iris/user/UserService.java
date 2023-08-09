package ir.mohaymen.iris.user;

import java.util.List;

public interface UserService {

    User getById(Long id);

    List<User> getById(List<Long> ids);

    Iterable<User> getByFirstName(String firstName);

    Iterable<User> getByLastName(String lastName);

    User getByPhoneNumber(String phoneNumber);

    User getByUserName(String userName);

    Iterable<User> getAll();

    User create(User user);

    void deleteById(Long id) throws Exception;

    void deleteByUserName(String userName) throws Exception;

    void deleteByPhoneNumber(String phoneNumber) throws Exception;
}
