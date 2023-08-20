package ir.mohaymen.iris.seeder;

import com.github.javafaker.Name;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;

    static final int NUMBER_OF_INSTANCES = 100;
    private final List<User> users = new ArrayList<>();

    @Override
    public void load() {
        if (userRepository.count() != 0) return;

        generateUser("ali", "hoseini", null, "0910", "something");
        for (int i = 0; i < NUMBER_OF_INSTANCES - 1; i++)
            generateRandomUser();
        generateUser("sadegh", "poolaiii", "poolai_23", "0911", null);
        userRepository.saveAll(users);
    }

    private void generateRandomUser() {
        long id = Long.parseLong(faker.regexify("\\d{1,5}"));
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = id % 4 == 2 ? name.lastName() : null;
        String userName = id % 5 == 3 ? name.username() + faker.regexify("(\\d|_){1,10}") : null;
        String phoneNumber = faker.phoneNumber().cellPhone().replaceAll("\\s", "");
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

        users.add(user);
    }
}
