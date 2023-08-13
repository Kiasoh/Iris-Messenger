package ir.mohaymen.iris.seeder;

import com.github.javafaker.Name;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;

    @Override
    public void load() {
        if (!userRepository.findAll().isEmpty()) return;

        final int NUMBER_OF_INSTANCES = 100;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            User user = generateRandomUser();
            userRepository.save(user);
        }
    }

    private User generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1,5}"));
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = id % 4 == 2 ? name.lastName() : null;
        String userName = name.username() + fakeValuesService.regexify("(\\d|_){1,10}");
        String phoneNumber = faker.phoneNumber().cellPhone();
        String bio = id % 3 == 0 ? name.title() + fakeValuesService.regexify("[\\w\\d\\s_,\\.]{1,50}") : null;

        User user = new User();
//        user.setUserId(id);
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setBio(bio);

        return user;
    }
}
