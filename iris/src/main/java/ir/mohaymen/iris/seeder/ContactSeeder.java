package ir.mohaymen.iris.seeder;

import com.github.javafaker.Name;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.contact.ContactRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ContactSeeder implements Seeder {

    private final ContactRepository contactRepository;

    static final int NUMBER_OF_INSTANCES = 200;
    private final List<Contact> contacts = new ArrayList<>();
    private final Map<Long, Set<Long>> userIds = new HashMap<>();

    @Override
    public void load() {
        if (contactRepository.count() != 0) return;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomContact();
        contactRepository.saveAll(contacts);
    }

    private void generateRandomContact() {
        long id = faker.random().nextInt(1, 99999);

        long firstUserId = faker.random().nextInt(1, UserSeeder.NUMBER_OF_INSTANCES);
        User firstUser = new User();
        firstUser.setUserId(firstUserId);

        userIds.computeIfAbsent(firstUserId, k -> new HashSet<>());

        long secondUserId;
        do {
            secondUserId = faker.random().nextInt(1, UserSeeder.NUMBER_OF_INSTANCES);
        } while (secondUserId == firstUserId || userIds.get(firstUserId).contains(secondUserId));
        User secondUser = new User();
        secondUser.setUserId(secondUserId);

        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = id % 4 == 0 ? name.lastName() : null;

        Contact contact = new Contact();
        contact.setFirstUser(firstUser);
        contact.setSecondUser(secondUser);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);

        contacts.add(contact);
        userIds.get(firstUserId).add(secondUserId);
    }
}
