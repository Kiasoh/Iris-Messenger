package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;

public interface PinService {

    Pin getById(Long id);

    Iterable<Pin> getAll();

    Pin getByMessage(Message message);

    Pin getByMessage(Long messageId) throws Exception;

    Iterable<Pin> getByChat(Chat chat);

    Iterable<Pin> getByChat(Long chatId) throws Exception;

    boolean isMessagePinned(Message message);

    Long numberOfPinsInChat(Chat chat);

    Long numberOfPinsInChat(Long chatId) throws Exception;

    Pin createOrUpdate(Pin pin);

    void deleteById(Long id) throws Exception;

    void delete(Pin pin);

    void deleteByChat(Chat chat);

    void deleteByChat(Long chatId) throws Exception;
}
