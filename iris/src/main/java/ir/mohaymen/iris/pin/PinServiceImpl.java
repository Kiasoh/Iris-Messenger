package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class PinServiceImpl implements PinService {

    private PinRepository pinRepository;
    private MessageRepository messageRepository;
    private ChatRepository chatRepository;

    @Override
    public Pin getById(Long id) {
        return pinRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Pin> getAll() {
        return pinRepository.findAll();
    }

    @Override
    public Pin getByMessage(Message message) {
        return pinRepository.findByMessage(message).orElse(null);
    }

    @Override
    public Pin getByMessage(Long messageId) throws Exception {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) return getByMessage(message);
        else throw new Exception("No such message exists.");
    }

    @Override
    public Iterable<Pin> getByChat(Chat chat) {
        return pinRepository.findByChat(chat);
    }

    @Override
    public Iterable<Pin> getByChat(Long chatId) throws Exception {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat != null) return getByChat(chat);
        else throw new Exception("No such chat exists.");
    }

    @Override
    public boolean isMessagePinned(Message message) {
        return getByMessage(message) != null;
    }

    @Override
    public Long numberOfPinsInChat(Chat chat) {
        Iterable<Pin> pins = getByChat(chat);
        return StreamSupport.stream(pins.spliterator(), false).count();
    }

    @Override
    public Long numberOfPinsInChat(Long chatId) throws Exception {
        Iterable<Pin> pins = getByChat(chatId);
        return StreamSupport.stream(pins.spliterator(), false).count();
    }

    @Override
    public Pin createOrUpdate(Pin pin) {
        return pinRepository.save(pin);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (getById(id) != null) pinRepository.deleteById(id);
        else throw new Exception("No such pin exists.");
    }

    @Override
    public void delete(Pin pin) {
        pinRepository.delete(pin);
    }

    @Override
    public void deleteByChat(Chat chat) {
        Iterable<Pin> pins = getByChat(chat);
        pinRepository.deleteAll(pins);
    }

    @Override
    public void deleteByChat(Long chatId) throws Exception {
        Iterable<Pin> pins = getByChat(chatId);
        pinRepository.deleteAll(pins);
    }
}
