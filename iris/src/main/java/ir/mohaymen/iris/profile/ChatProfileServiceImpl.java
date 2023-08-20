package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatRepository;
import ir.mohaymen.iris.media.Media;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatProfileServiceImpl implements ChatProfileService {

    private final ChatProfileRepository chatProfileRepository;
    private final ChatRepository chatRepository;

    @Override
    public Iterable<ChatProfile> getAll() {
        return chatProfileRepository.findAll();
    }

    @Override
    public ChatProfile getById(Long id) {
        return chatProfileRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<ChatProfile> getByChat(Chat chat) {
        return chatProfileRepository.findByChat(chat);
    }

    @Override
    public Iterable<ChatProfile> getByMedia(Media media) {
        return chatProfileRepository.findAllByMedia(media);
    }

    @Override
    public ChatProfile createOrUpdate(ChatProfile chatProfile) {
        return chatProfileRepository.save(chatProfile);
    }

    @Override
    public ChatProfile getLastChatProfile(Chat chat) {
        return chatProfileRepository.findFirstByChatOrderByIdChatDesc(chat);
    }

    @Override
    public void deleteById(Long id) {
        chatProfileRepository.deleteById(id);
    }

    public Iterable<ChatProfile> getByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(EntityNotFoundException::new);
        return getByChat(chat);
    }
}
