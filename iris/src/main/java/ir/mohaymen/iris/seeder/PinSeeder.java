package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
import ir.mohaymen.iris.pin.Pin;
import ir.mohaymen.iris.pin.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PinSeeder implements Seeder {

    private final PinRepository pinRepository;
    private final MessageRepository messageRepository;

    @Override
    public void load() {
        final int NUMBER_OF_INSTANCES = 40;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            Pin pin = generateRandomUser();
            if (pin != null) pinRepository.save(pin);
        }
    }

    private Pin generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1-5}"));

        long messageId = Long.parseLong(fakeValuesService.regexify("\\d{3}"));
        Message message = messageRepository.findById(messageId).orElse(null);

        if (message == null) return null;

        Chat chat = message.getOriginChat();

        Pin pin = new Pin();
        pin.setPinId(id);
        pin.setMessage(message);
        pin.setChat(chat);
        return pin;
    }
}
