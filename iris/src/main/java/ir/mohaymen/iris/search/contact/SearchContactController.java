package ir.mohaymen.iris.search.contact;

import ir.mohaymen.iris.utility.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search/contact")
public class SearchContactController extends BaseController {

    private final SearchContactService searchContactService;
//    private final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @GetMapping("{name}")
    public ResponseEntity<List<SearchContactDto>> searchByName(String name){
//        logger.info(MessageFormat.format("", ));
        return new ResponseEntity<>(searchContactService.searchByName(name, getUserByToken().getUserId()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> indexContact(@RequestBody SearchContactDto contact){
//        logger.info(MessageFormat.format());
        return new ResponseEntity<>(searchContactService.index(contact), HttpStatus.OK);
    }
}
