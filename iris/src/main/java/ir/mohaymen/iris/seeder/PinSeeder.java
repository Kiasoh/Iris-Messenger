package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.pin.Pin;
import ir.mohaymen.iris.pin.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PinSeeder implements Seeder {

    private final PinRepository pinRepository;

    @Override
    public void load() {
        if (pinRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 40;
        final List<Pin> pins = new ArrayList<>();
        final List<Long> messageIds = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomPin(pins, messageIds);
        pinRepository.saveAll(pins);
    }

    private void generateRandomPin(List<Pin> pinList, List<Long> messageIdList) {
        long id = Long.parseLong(faker.regexify("\\d{1,5}"));

        long messageId;
        do {
            messageId = faker.random().nextInt(1, 1000);
        } while (messageIdList.contains(messageId));
        Message message = new Message();
        message.setMessageId(messageId);

        Chat chat = message.getOriginChat();

        Pin pin = new Pin();
        pin.setPinId(id);
        pin.setMessage(message);
        pin.setChat(chat);

        messageIdList.add(messageId);
        pinList.add(pin);
    }
}
