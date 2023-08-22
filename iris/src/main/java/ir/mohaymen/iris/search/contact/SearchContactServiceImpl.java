package ir.mohaymen.iris.search.contact;

import lombok.AllArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
public class SearchContactServiceImpl implements SearchContactService{

    private static final String INDEX_NAME = "contact";
    private final SearchContactRepository searchContactRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public String index(SearchContactDto contact) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(contact.getId().toString())
                .withObject(contact)
                .build();

        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_NAME));
    }

    public List<String> bulkIndex(List<SearchContactDto> contacts){

        List<IndexQuery> queries =
                contacts.stream()
                .map(contact -> new IndexQueryBuilder()
                        .withId(contact.getId().toString())
                        .withObject(contact)
                        .build())
                .collect(Collectors.toList());

        return elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(INDEX_NAME));
    }



    @Override
    public void deleteById(Long id) {
        searchContactRepository.deleteById(id);
    }

    @Override
    public List<SearchContactDto> searchByName(String name, Long userId) {

        MultiMatchQueryBuilder query = QueryBuilders
                .multiMatchQuery(name)
                .field("firstName")
                .field("lastName")
                .fuzziness(Fuzziness.AUTO);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        SearchHits<SearchContactDto> hits = elasticsearchOperations.search(searchQuery, SearchContactDto.class, IndexCoordinates.of(INDEX_NAME));

        return hits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }


}
