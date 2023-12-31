package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Override
    public Contact getById(Long id) {
        return contactRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Iterable<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> getContactByFirstUser(User firstUser) {
        return contactRepository.findByFirstUser(firstUser);
    }
    @Override
    public boolean isInContact (User firstUser , Long secondUserId) {
        return contactRepository.userInContact(firstUser.getUserId() , secondUserId);
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
    public Contact getContact(Long firstUserId, Long secondUserId) {
        return contactRepository.getContact(firstUserId , secondUserId);
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
        User user = userRepository.findById(firstUserId).orElseThrow(EntityNotFoundException::new);;
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
        User user = userRepository.findById(secondUserId).orElseThrow(EntityNotFoundException::new);
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
