package ir.mohaymen.iris.chat;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;

    @Override
    public Chat getById(Long id) {
        return chatRepository.findById(id).orElse(null);
    }

    @Override
    public Chat getByLink(String link) {
        return chatRepository.findByLink(link).orElse(null);
    }

    @Override
    public Iterable<Chat> getByTitle(String title) {
        return chatRepository.findByTitle(title);
    }

    @Override
    public Iterable<Chat> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public Chat createOrUpdate(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (getById(id) != null) chatRepository.deleteById(id);
        else throw new Exception("No such chat exists.");
    }
}
