package ir.mohaymen.iris.search.chat;

import ir.mohaymen.iris.search.contact.SearchContactDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchChatRepository extends ElasticsearchRepository<SearchChatDto, Long> {

    void deleteById(Long id);
}
