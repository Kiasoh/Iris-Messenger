package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;

public interface PinService {

    Pin getById(Long id);

    Iterable<Pin> getAll();

    Pin getByMessage(Message message);

    Iterable<Pin> getByChat(Chat chat);

    boolean isMessagePinned(Message message);

    Long numberOfPinsInChat(Chat chat);

    Pin createOrUpdate(Pin pin);

    void deleteById(Long id);

    void delete(Pin pin);

    void deleteByMessage(Message message);

    void deleteByChat(Chat chat);
}
