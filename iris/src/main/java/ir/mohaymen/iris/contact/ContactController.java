package ir.mohaymen.iris.contact;

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
        return new ResponseEntity<>( makePostContact(contact) , HttpStatus.OK);
    }
    @GetMapping("/get-contacts")
    public ResponseEntity<List<PostContactDto>> getContacts() {
        List<PostContactDto> pcdtl = new ArrayList<>();
        for (Contact con: getUserByToken().getContacts() ) {
            pcdtl.add(makePostContact(con));
        }
        return new ResponseEntity<>(pcdtl, HttpStatus.OK);
    }
    @GetMapping("/get-contact")
    public ResponseEntity<PostContactDto> getContact(@RequestBody Long userId) {
        if(!contactService.isInContact(getUserByToken() , userId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(makePostContact(contactService.getContact(getUserByToken().getUserId(), userId)) , HttpStatus.OK);
    }
    public PostContactDto makePostContact(Contact con){
        PostContactDto postContactDto = modelMapper.map(con , PostContactDto.class);
        postContactDto.setSecondUserId(con.getSecondUser().getUserId());
        return postContactDto;
    }
}