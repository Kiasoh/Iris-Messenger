package ir.mohaymen.iris.search.chat;

import lombok.AllArgsConstructor;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchChatServiceImpl implements SearchChatService {

        private static final String INDEX_NAME = "chat";
        private final SearchChatRepository searchChatRepository;
        private final ElasticsearchOperations elasticsearchOperations;

        @Override
        public String index(SearchChatDto searchChatDto) {
                IndexQuery indexQuery = new IndexQueryBuilder()
                                .withId(searchChatDto.getId().toString())
                                .withObject(searchChatDto)
                                .build();

                return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_NAME));
        }

        @Override
        public List<String> bulkIndex(List<SearchChatDto> chats) {
                List<IndexQuery> indexQueries = chats.stream()
                                .map(chat -> new IndexQueryBuilder()
                                                .withId(chat.getId().toString())
                                                .withObject(chat)
                                                .build())
                                .collect(Collectors.toList());

                return elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(INDEX_NAME));
        }

        @Override
        public void deleteById(Long id) {
                searchChatRepository.deleteById(id);
        }

        @Override
        public List<SearchChatDto> search(String text, Long userId) {

                MatchQueryBuilder titleQuery = QueryBuilders
                                .matchQuery("title", text)
                                .fuzziness(Fuzziness.AUTO);

                TermQueryBuilder userQuery = QueryBuilders
                                .termQuery("userId", userId);


                Query searchQuery = new NativeSearchQueryBuilder()
                                .withFilter(userQuery)
                                .withQuery(titleQuery)
                                .build();

                SearchHits<SearchChatDto> hits = elasticsearchOperations.search(searchQuery, SearchChatDto.class,
                                IndexCoordinates.of(INDEX_NAME));

                return hits.stream()
                                .map(hit -> hit.getContent())
                                .collect(Collectors.toList());
        }
}
