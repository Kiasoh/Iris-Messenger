package ir.mohaymen.iris.search.contact;

import ir.mohaymen.iris.contact.ContactController;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search/contact")
public class SearchContactController {

    private final SearchContactService searchContactService;
    private final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @GetMapping("/{name}")
    public ResponseEntity<List<SearchContactDto>> searchByName(@PathVariable String name){
        return new ResponseEntity<>(searchContactService.searchByName(name), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> indexContact(@RequestBody SearchContactDto contact){
//        logger.info(MessageFormat.format());
        return new ResponseEntity<>(searchContactService.index(contact), HttpStatus.OK);
    }
}
