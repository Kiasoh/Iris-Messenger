package ir.mohaymen.iris.search.message;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchMessageService searchMessageService;

    @GetMapping("{text}")
    public ResponseEntity<?> searchByText(@PathVariable String text){
        return new ResponseEntity<>(searchMessageService.searchByText(text), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> search(){
        return new ResponseEntity<>(searchMessageService.searchAll(), HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<String> indexMessage(@RequestBody SearchMessageDto message){
        String response = searchMessageService.create(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/multi")
    public ResponseEntity<List<String>> indexListOfMessages(@RequestBody List<SearchMessageDto> messages){
        List<String> responses = searchMessageService.createBulk(messages);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id){
        searchMessageService.deleteById(id);
        return new ResponseEntity<>("successful", HttpStatus.OK);
    }
//    @PostMapping("")
//    public ResponseEntity<List<SearchMessageDto>> getMatchedMessages(@PathVariable String text){
//        List<SearchMessageDto> messages = searchMessageService.createMessageIndexBulk()
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }
}
