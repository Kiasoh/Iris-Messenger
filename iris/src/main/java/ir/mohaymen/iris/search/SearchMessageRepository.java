package ir.mohaymen.iris.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchMessageRepository extends ElasticsearchRepository<SearchMessageDto, Long> {

    SearchMessageDto findById(Long id);
}
