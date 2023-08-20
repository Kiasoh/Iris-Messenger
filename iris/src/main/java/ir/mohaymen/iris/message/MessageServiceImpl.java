package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Override
    public Message getById(Long id) {
        return messageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public long countUnSeenMessages(Long lastSeenMessageId, Long chatId) {
        return messageRepository.countUnSeenMessages(lastSeenMessageId, chatId);
    }

    @Override
    public List<Subscription> usersSeen(Long messageId, Long chatId) {
        return messageRepository.usersSeen(messageId, chatId);
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
    public List<Message> getByChat(Chat chat) {
        return messageRepository.findByOriginChat(chat);
    }

    @Override
    public Iterable<Message> getByMedia(Media media) {
        return messageRepository.findAllByMedia(media);
    }

    @Override
    public Message createOrUpdate(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public void deleteByUser(User user) {
        messageRepository.deleteBySender(user);
    }

    @Override
    public void deleteByChat(Chat chat) {
        messageRepository.deleteByOriginChat(chat);
    }

    public Iterable<Message> getByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        return getByUser(user);
    }

    public Iterable<Message> getByUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(EntityNotFoundException::new);
        return getByUser(user);
    }

    public Iterable<Message> getByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(EntityNotFoundException::new);
        return getByChat(chat);
    }

    public void deleteByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        deleteByUser(user);
    }

    public void deleteByUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(EntityNotFoundException::new);
        deleteByUser(user);
    }

    public void deleteByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(EntityNotFoundException::new);
        deleteByChat(chat);
    }

    public Page<Message> getMessagesByPage(Long chatId, int pageNum, int pageSize) {
//        Iterable<Message> messages = getByChat(chatId);
        var chat = new Chat();
        chat.setChatId(chatId);
        var message = new Message();
        message.setOriginChat(chat);
        return messageRepository.findAll(Example.of(message), Pageable.ofSize(pageSize).withPage(pageNum));
//         messageRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNum));
    }
    @Override
    public Message getLastMessageByChatId(Long chatId){
        return messageRepository.getLastMessageByChatId(chatId);
    }
}
