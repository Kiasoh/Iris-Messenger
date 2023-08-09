package ir.mohaymen.iris.chat;

public interface ChatService {

    Chat getById(Long id);

    Chat getByLink(String link);

    Iterable<Chat> getByTitle(String title);

    Iterable<Chat> getAll();

    Chat createOrUpdate(Chat chat);

    void deleteById(Long id) throws Exception;
}
