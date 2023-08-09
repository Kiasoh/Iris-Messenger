package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatProfileServiceImpl implements ChatProfileService {

    private final ChatProfileRepository chatProfileRepository;
    private final ChatRepository chatRepository;

    @Override
    public Iterable<ChatProfile> getAll() {
        return chatProfileRepository.findAll();
    }

    @Override
    public ChatProfile getById(Long id) {
        return chatProfileRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<ChatProfile> getByChat(Chat chat) {
        return chatProfileRepository.findByChat(chat);
    }

    @Override
    public ChatProfile createOrUpdate(ChatProfile chatProfile) {
        return chatProfileRepository.save(chatProfile);
    }

    @Override
    public void deleteById(Long id) {
        chatProfileRepository.deleteById(id);
    }

    public Iterable<ChatProfile> getByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        return getByChat(chat);
    }
}
