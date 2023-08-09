package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatProfileServiceImpl implements ChatProfileService{

    private final ChatProfileRepository chatProfileRepository;

    @Override
    public Iterable<ChatProfile> get() {
        return chatProfileRepository.findAll();
    }

    @Override
    public ChatProfile getById(Long id) {
        return chatProfileRepository.findById(id).orElse(null);
    }

    @Override
    public ChatProfile create(ChatProfile chatProfile) {
        return chatProfileRepository.save(chatProfile);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if(getById(id) != null)
            chatProfileRepository.deleteById(id);
        else
            throw new Exception();
    }
}
