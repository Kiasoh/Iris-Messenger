package ir.mohaymen.iris.search.contact;

import ir.mohaymen.iris.search.message.SearchMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchContactRepository extends ElasticsearchRepository<SearchContactDto, Long> {

    void deleteById(Long id);
}
