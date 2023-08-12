package ir.mohaymen.iris.seeder;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ChatSeeder implements Seeder {

    private final ChatRepository chatRepository;

    @Override
    public void load() {
        final int NUMBER_OF_INSTANCES = 50;
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
        FakeValuesService fakeValuesService = new FakeValuesService(locale, new RandomService());
        Faker faker = new Faker(locale);

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            Chat chat= generateRandomUser(fakeValuesService, faker);
            chatRepository.save(chat);
        }
    }

    private Chat generateRandomUser(FakeValuesService fakeValuesService, Faker faker) {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1-5}"));
        String title  = faker.book().title();
        String link = id%5==1 ? fakeValuesService.regexify("\\w[\\w\\d_]{2,10}"):null;
        boolean isPublic = id%2==0;
        ChatType chatType = ChatType.values()[(int) id%ChatType.values().length];
        String bio = id % 3 == 1 ? fakeValuesService.regexify("[\\w\\d\\s_,\\.]{1,50}") : null;


        Chat chat = new Chat();
        chat.setChatId(id);
        chat.setTitle(title);
        chat.setLink(link);
        chat.setPublic(isPublic);
        chat.setChatType(chatType);
        chat.setBio(bio);
        chat.setBio(bio);
//        chat.setSubs();

        return chat;
    }
}
