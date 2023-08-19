package ir.mohaymen.iris.search;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchMessageRepository extends ElasticsearchRepository<SearchMessageDto, Long> {

    Page<SearchMessageDto> findById(Long id);

    void deleteById(Long id);
}
