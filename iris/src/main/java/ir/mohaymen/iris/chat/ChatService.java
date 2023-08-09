package ir.mohaymen.iris.chat;

public interface ChatService {

    Chat getById(Long id);

    Iterable<Chat> getAll();

    Chat createOrUpdate(Chat chat);

    void deleteById(Long id) throws Exception;
}
