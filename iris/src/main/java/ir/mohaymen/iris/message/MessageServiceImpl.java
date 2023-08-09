package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ChatRepository chatRepository;

    @Override
    public Message getById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public Iterable<Message> getByUser(User user) {
        return messageRepository.findBySender(user);
    }

    @Override
    public Iterable<Message> getByUser(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) return getByUser(user);
        else throw new Exception("No such user exists.");
    }

    @Override
    public Iterable<Message> getByUser(String userName) throws Exception {
        User user = userRepository.findByUserName(userName).orElse(null);
        if (user != null) return getByUser(user);
        else throw new Exception("No such user exists.");
    }

    @Override
    public Iterable<Message> getByChat(Chat chat) {
        return messageRepository.findByOriginChat(chat);
    }

    @Override
    public Iterable<Message> getByChat(Long chatId) throws Exception {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat != null) return getByChat(chat);
        else throw new Exception("No such chat exists");
    }

    @Override
    public Message createOrUpdate(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (getById(id) != null) messageRepository.deleteById(id);
        else throw new Exception("No such message exists.");
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public void deleteByUser(User user) {
        Iterable<Message> messages = getByUser(user);
        messageRepository.deleteAll(messages);
    }

    @Override
    public void deleteByUser(Long userId) throws Exception {
        Iterable<Message> messages = getByUser(userId);
        messageRepository.deleteAll(messages);
    }

    @Override
    public void deleteByUser(String userName) throws Exception {
        Iterable<Message> messages = getByUser(userName);
        messageRepository.deleteAll(messages);
    }

    @Override
    public void deleteByChat(Chat chat) {
        Iterable<Message> messages = getByChat(chat);
        messageRepository.deleteAll(messages);
    }

    @Override
    public void deleteByChat(Long chatId) throws Exception {
        Iterable<Message> messages = getByChat(chatId);
        messageRepository.deleteAll(messages);
    }
}
