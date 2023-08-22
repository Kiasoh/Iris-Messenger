package ir.mohaymen.iris.search.chat;

import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatServiceImpl;
import ir.mohaymen.iris.search.contact.SearchContactDto;
import ir.mohaymen.iris.user.User;
import lombok.AllArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchChatServiceImpl implements SearchChatService{

    private static final String INDEX_NAME = "chat";
    private final SearchChatRepository searchChatRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ChatService chatService;


    @Override
    public String index(SearchChatDto searchChatDto) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(searchChatDto.getId().toString())
                .withObject(searchChatDto)
                .build();

        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_NAME));
    }

    @Override
    public List<String> bulkIndex(List<SearchChatDto> chats, Long userId){
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

        MatchQueryBuilder userQuery = QueryBuilders
                .matchQuery("userId", userId);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(titleQuery)
                .withFilter(userQuery)
                .build();

        SearchHits<SearchChatDto> hits = elasticsearchOperations.search(searchQuery, SearchChatDto.class, IndexCoordinates.of(INDEX_NAME));

        return hits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }
}
