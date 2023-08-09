package ir.mohaymen.iris.user;

public interface UserService {

    User getById(Long id);

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
