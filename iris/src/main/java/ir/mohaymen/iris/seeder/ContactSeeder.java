package ir.mohaymen.iris.seeder;

import com.github.javafaker.Name;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.contact.ContactRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ContactSeeder implements Seeder {

    private final ContactRepository contactRepository;

    @Override
    public void load() {
        if (contactRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 200;
        final List<Contact> contacts = new ArrayList<>();
        final Map<Long, List<Long>> userIds = new HashMap<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomContact(contacts, userIds);
        contactRepository.saveAll(contacts);
    }

    private void generateRandomContact(List<Contact> contactList, Map<Long, List<Long>> ids) {
        long id = Long.parseLong(faker.regexify("\\d{1,5}"));

        long firstUserId = faker.random().nextInt(1, 100);
        User firstUser = new User();
        firstUser.setUserId(firstUserId);

        ids.computeIfAbsent(firstUserId, k -> new ArrayList<>());

        long secondUserId;
        do {
            secondUserId = faker.random().nextInt(1, 100);
        } while (secondUserId == firstUserId || ids.get(firstUserId).contains(secondUserId));
        User secondUser = new User();
        secondUser.setUserId(secondUserId);

        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = id % 5 == 0 ? name.lastName() : null;

        Contact contact = new Contact();
        contact.setFirstUser(firstUser);
        contact.setSecondUser(secondUser);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);

        contactList.add(contact);
        ids.get(firstUserId).add(secondUserId);
    }
}
