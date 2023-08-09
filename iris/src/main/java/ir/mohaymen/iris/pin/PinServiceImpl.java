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

    private final PinRepository pinRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

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
    public Iterable<Pin> getByChat(Chat chat) {
        return pinRepository.findByChat(chat);
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
    public Pin createOrUpdate(Pin pin) {
        return pinRepository.save(pin);
    }

    @Override
    public void deleteById(Long id) {
        pinRepository.deleteById(id);
    }

    @Override
    public void delete(Pin pin) {
        pinRepository.delete(pin);
    }

    @Override
    public void deleteByMessage(Message message) {
        pinRepository.deleteByMessage(message);
    }

    @Override
    public void deleteByChat(Chat chat) {
        pinRepository.deleteByChat(chat);
    }

    public Pin getByMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        return getByMessage(message);
    }

    public Iterable<Pin> getByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        return getByChat(chat);
    }

    public Long numberOfPinsInChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        return numberOfPinsInChat(chat);
    }

    public void deleteByMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        deleteByMessage(message);
    }

    public void deleteByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        deleteByChat(chat);
    }
}
