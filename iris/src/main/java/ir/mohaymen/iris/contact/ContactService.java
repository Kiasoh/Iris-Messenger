package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;

public interface ContactService {

    Contact getById(Long id);

    Iterable<Contact> getAll();

    Iterable<Contact> getContactByFirstUser(Long firstUserId) throws Exception;

    Iterable<Contact> getContactByFirstUser(User firstUser);

    Iterable<User> getUserByFirstUser(Long firstUserId) throws Exception;

    Iterable<User> getUserByFirstUser(User firstUser);

    Iterable<Contact> getContactBySecondUser(Long secondUserId) throws Exception;

    Iterable<Contact> getContactBySecondUser(User secondUser);

    Iterable<User> getUserBySecondUser(Long secondUserId) throws Exception;

    Iterable<User> getUserBySecondUser(User secondUser);

    Contact createOrUpdate(Contact contact);

    void deleteById(Long id) throws Exception;

    void deleteByFirstUser(Long firstUserName) throws Exception;

    void deleteByFirstUser(User firstUSer);

    void deleteBySecondUser(Long secondUserName) throws Exception;

    void deleteBySecondUser(User secondUser);
}
