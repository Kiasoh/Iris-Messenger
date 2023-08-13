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

        List<Contact> contacts = new ArrayList<>();
        Map<Long, List<Long>> ids = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomContact(contacts, ids);
        contactRepository.saveAll(contacts);
    }

    private void generateRandomContact(List<Contact> contactList, Map<Long, List<Long>> ids) {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1,5}"));

        long firstUserId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
        User firstUser = new User();
        firstUser.setUserId(firstUserId);

        long secondUserId;
        do {
            secondUserId = Long.parseLong(fakeValuesService.regexify("[1-9][0-9]?|100"));
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
        if (ids.get(firstUserId) != null) ids.get(firstUserId).add(secondUserId);
        else ids.put(firstUserId, List.of(secondUserId));
    }
}
