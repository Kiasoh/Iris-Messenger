package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.chat.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class ChatSeeder implements Seeder {

    private final ChatRepository chatRepository;

    @Override
    public void load() {
        final int NUMBER_OF_INSTANCES = 100;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            Chat chat = generateRandomUser();
            chatRepository.save(chat);
        }
    }

    private Chat generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1,5}"));
        String title = faker.book().title();
        String link = id % 5 == 0 ? fakeValuesService.regexify("\\w(\\w|\\d|_){2,10}") : null;
        boolean isPublic = id % 2 == 0;
        ChatType chatType = ChatType.values()[(int) id % ChatType.values().length];
        String bio = id % 3 == 0 ? fakeValuesService.regexify("(\\w|\\d| |,|\\.){5,50}") : null;

        Chat chat = new Chat();
        chat.setChatId(id);
        chat.setTitle(title);
        chat.setLink(link);
        chat.setPublic(isPublic);
        chat.setChatType(chatType);
        chat.setBio(bio);
        chat.setSubs(new HashSet<>());

        return chat;
    }
}
