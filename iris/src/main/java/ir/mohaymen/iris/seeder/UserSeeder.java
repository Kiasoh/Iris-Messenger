package ir.mohaymen.iris.seeder;

import com.github.javafaker.Name;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;

    static final int NUMBER_OF_INSTANCES = 30;
    private final List<User> users = new ArrayList<>();

    @Override
    public void load() {
        if (userRepository.count() != 0) return;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomUser();

        userRepository.saveAll(users);
        clearReferences();
    }

    @Override
    public void clearReferences() {
        users.clear();
    }

    private void generateRandomUser() {
        long id = faker.random().nextInt(1, 99999);
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = id % 4 == 2 ? name.lastName() : null;
        String userName = id % 5 == 3 ? name.username() + faker.regexify("(\\d|_){1,10}") : null;
        String phoneNumber = faker.regexify("09\\d{9}");
        String bio = id % 3 == 0 ? name.title() + faker.regexify("(\\w|\\d| |_|,|\\.){1,50}") : null;

        generateUser(firstName, lastName, userName, phoneNumber, bio);
    }

    private void generateUser(String firstName, String lastName, String userName, String phoneNumber, String bio) {
        User user = new User();
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setBio(bio);
        user.setLastSeen(Instant.now());

        users.add(user);
    }
}
