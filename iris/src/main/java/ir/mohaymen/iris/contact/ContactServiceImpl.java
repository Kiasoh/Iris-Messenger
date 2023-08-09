package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private ContactRepository contactRepository;
    private UserRepository userRepository;

    @Override
    public Contact getById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Iterable<Contact> getContactByFirstUser(User firstUser) {
        return contactRepository.findByFirstUser(firstUser);
    }

    @Override
    public Iterable<User> getUserByFirstUser(User firstUser) {
        Iterable<Contact> contacts = getContactByFirstUser(firstUser);
        ArrayList<User> users = new ArrayList<>();
        for (Contact contact : contacts)
            users.add(contact.getSecondUser());
        return users;
    }

    @Override
    public Iterable<Contact> getContactBySecondUser(User secondUser) {
        return contactRepository.findBySecondUser(secondUser);
    }

    @Override
    public Iterable<User> getUserBySecondUser(User secondUser) {
        Iterable<Contact> contacts = getContactBySecondUser(secondUser);
        ArrayList<User> users = new ArrayList<>();
        for (Contact contact : contacts)
            users.add(contact.getSecondUser());
        return users;
    }

    @Override
    public Contact createOrUpdate(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void deleteById(Long id) {
        Contact contact = getById(id);
        contactRepository.delete(contact);
    }

    @Override
    public void deleteByFirstUser(User firstUSer) {
        Iterable<Contact> contacts = getContactByFirstUser(firstUSer);
        contactRepository.deleteAll(contacts);
    }

    @Override
    public void deleteBySecondUser(User secondUser) {
        Iterable<Contact> contacts = getContactBySecondUser(secondUser);
        contactRepository.deleteAll(contacts);
    }

    public Iterable<Contact> getContactByFirstUser(Long firstUserId) {
        User user = userRepository.findById(firstUserId).orElse(null);
        return getContactByFirstUser(user);
    }

    public Iterable<User> getUserByFirstUser(Long firstUserId) {
        Iterable<Contact> contacts = getContactByFirstUser(firstUserId);
        ArrayList<User> users = new ArrayList<>();
        for (Contact contact : contacts)
            users.add(contact.getSecondUser());
        return users;
    }

    public Iterable<Contact> getContactBySecondUser(Long secondUserId) {
        User user = userRepository.findById(secondUserId).orElse(null);
        return getContactByFirstUser(user);
    }

    public Iterable<User> getUserBySecondUser(Long secondUserId) {
        Iterable<Contact> contacts = getContactBySecondUser(secondUserId);
        ArrayList<User> users = new ArrayList<>();
        for (Contact contact : contacts)
            users.add(contact.getSecondUser());
        return users;
    }

    public void deleteByFirstUser(Long firstUserName) {
        Iterable<Contact> contacts = getContactByFirstUser(firstUserName);
        contactRepository.deleteAll(contacts);
    }

    public void deleteBySecondUser(Long secondUserName) {
        Iterable<Contact> contacts = getContactBySecondUser(secondUserName);
        contactRepository.deleteAll(contacts);
    }
}
