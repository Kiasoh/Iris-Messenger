package ir.mohaymen.iris.search.message;

import lombok.AllArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchMessageService {

    private static final String INDEX_NAME = "message";
    private final SearchMessageRepository searchMessageRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public List<SearchMessageDto> searchByText(String text){
        QueryBuilder query = QueryBuilders
                .matchQuery("text", text)
                .fuzziness(Fuzziness.AUTO);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        SearchHits<SearchMessageDto> searchHits =
                elasticsearchOperations.search(searchQuery, SearchMessageDto.class, IndexCoordinates.of(INDEX_NAME));

        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

    }

    public List<SearchMessageDto> searchAll(){
        return searchMessageRepository.findAll(Pageable.unpaged()).toList();
    }

    public String create(SearchMessageDto message){
        IndexQuery query = new IndexQueryBuilder()
                .withId(message.getId().toString())
                .withObject(message)
                .build();

        return elasticsearchOperations.index(query, IndexCoordinates.of(INDEX_NAME));
    }

    public List<String> createBulk(List<SearchMessageDto> messages){
        List<IndexQuery> queries = messages.stream()
                .map(message -> new IndexQueryBuilder()
                        .withId(message.getId().toString())
                        .withObject(message)
                        .build())
                .collect(Collectors.toList());

        return elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(INDEX_NAME));
    }

    public void deleteById(Long id){
        searchMessageRepository.deleteById(id);
    }
}
