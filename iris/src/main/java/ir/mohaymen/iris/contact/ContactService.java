package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;

import java.util.List;

public interface ContactService {

    Contact getById(Long id);

    Iterable<Contact> getAll();

    List<Contact> getContactByFirstUser(User firstUser);

    Iterable<User> getUserByFirstUser(User firstUser);
    boolean isInContact (User firstUser , Long secondUserId);

    Iterable<Contact> getContactBySecondUser(User secondUser);

    Iterable<User> getUserBySecondUser(User secondUser);
    Contact getContact(Long firstUserId , Long secondUserId);

    Contact createOrUpdate(Contact contact);

    void deleteById(Long id) throws Exception;

    void deleteByFirstUser(User firstUSer);

    void deleteBySecondUser(User secondUser);
}
