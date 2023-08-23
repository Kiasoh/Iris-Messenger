package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.search.contact.SearchContactDto;
import ir.mohaymen.iris.search.contact.SearchContactService;
import ir.mohaymen.iris.search.message.SearchMessageService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController extends BaseController {
    private final UserService userService;
    private final ContactService contactService;
    private final ModelMapper modelMapper;
    private final SearchContactService searchContactService;

    @PostMapping("/add-contact")
    public ResponseEntity<PostContactDto> addContact(@RequestBody @Valid GetContactDto getContactDto) {
        if (contactService.isInContact(getUserByToken() , getContactDto.getContactId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Contact contact = modelMapper.map(getContactDto, Contact.class);
        User user = userService.getById(getContactDto.getContactId());
        contact.setSecondUser(user);
        contact.setId(null);
        contact.setFirstUser(getUserByToken());
        contact = contactService.createOrUpdate(contact);
        searchContactService.index(new SearchContactDto(contact.getId(), contact.getFirstUser().getUserId(), contact.getSecondUser().getUserId(), contact.getFirstName(), contact.getLastName()));
        return new ResponseEntity<>( makePostContact(contact) , HttpStatus.OK);
    }
    @GetMapping("/get-contacts")
    public ResponseEntity<List<PostContactDto>> getContacts() {
        List<PostContactDto> pcdtl = new ArrayList<>();
        List<Contact> contacts = contactService.getContactByFirstUser(getUserByToken());
        contacts.stream()
                .forEach(c -> pcdtl.add(makePostContact(c)));
        return new ResponseEntity<>(pcdtl, HttpStatus.OK);
    }
    @GetMapping("/get-contact-by-user")
    public ResponseEntity<PostContactDto> getContactBySecondUser(@RequestParam Long userId) {
        if(!contactService.isInContact(getUserByToken() , userId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(makePostContact(contactService.getContact(getUserByToken().getUserId(), userId)) , HttpStatus.OK);
    }
    @GetMapping("/get-contact/{contactId}")
    public ResponseEntity<PostContactDto> getContact(   @PathVariable Long contactId) {
        Contact contact = contactService.getById(contactId);
        if (contact.getFirstUser().getUserId() != getUserByToken().getUserId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(makePostContact(contact) , HttpStatus.OK);
    }
    public PostContactDto makePostContact(Contact con){
        PostContactDto postContactDto = modelMapper.map(con , PostContactDto.class);
        postContactDto.setSecondUserId(con.getSecondUser().getUserId());
        return postContactDto;
    }
    @DeleteMapping("/delete-contact/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id){
        if (!contactService.isInContact(getUserByToken(), id)) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        contactService.deleteBySecondUser(userService.getById(id));
        return ResponseEntity.ok("contact deleted");
    }
}