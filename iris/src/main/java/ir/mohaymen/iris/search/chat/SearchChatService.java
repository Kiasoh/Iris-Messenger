package ir.mohaymen.iris.search.chat;

import ir.mohaymen.iris.search.contact.SearchContactDto;

import java.util.List;

public interface SearchChatService {

    String index(SearchChatDto chat);

    List<String> bulkIndex(List<SearchChatDto> chats);

    void deleteById(Long id);

    List<SearchChatDto> search(String text, Long userId);
}
