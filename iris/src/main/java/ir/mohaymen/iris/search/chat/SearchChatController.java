package ir.mohaymen.iris.search.chat;

import ir.mohaymen.iris.utility.BaseController;
import lombok.AllArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search/chat")
@AllArgsConstructor
public class SearchChatController extends BaseController {

    private final SearchChatService searchChatService;

    @GetMapping("/{text}")
    public ResponseEntity<List<SearchChatDto>> search(@PathVariable String text){
        List<SearchChatDto> result = searchChatService.search(text, getUserByToken().getUserId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> index(@RequestBody SearchChatDto chat) {
        String indexedId = searchChatService.index(chat);
        return new ResponseEntity<>(indexedId, HttpStatus.OK);
    }

    @PostMapping("/multi")
    public ResponseEntity<List<String>> bulkIndex(@RequestBody List<SearchChatDto> chats){
        List<String> indexIds = searchChatService.bulkIndex(chats, getUserByToken().getUserId());
        return new ResponseEntity<>(indexIds, HttpStatus.OK);
    }
}
