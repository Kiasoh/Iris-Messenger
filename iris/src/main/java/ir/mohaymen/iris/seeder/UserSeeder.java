package ir.mohaymen.iris.seeder;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;

    @Override
    public void load() {
        final int NUMBER_OF_INSTANCES = 50;
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
        FakeValuesService fakeValuesService = new FakeValuesService(locale, new RandomService());
        Faker faker = new Faker(locale);

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            User user = generateRandomUser(fakeValuesService, faker);
            userRepository.save(user);
        }
    }

    private User generateRandomUser(FakeValuesService fakeValuesService, Faker faker){
        long id= Long.parseLong(fakeValuesService.regexify("\\d{1-5}"));
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = id%4==2 ?  name.lastName() : null;
        String userName = name.username() + fakeValuesService.regexify("[\\d_]{1,10}");
        String phoneNumber = faker.phoneNumber().phoneNumber();
        String bio = id%3==1 ? fakeValuesService.regexify("[\\w\\d\\s_,\\.]{1,50}") : null;

        User user = new User();
        user.setUserId(id);
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setBio(bio);

        return user;
    }
}
