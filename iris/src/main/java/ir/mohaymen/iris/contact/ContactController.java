package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
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
    public ResponseEntity<PostContactDto> addContact(@RequestBody GetContactDto getContactDto) {
        if (contactService.isInContact(getUserByToken() , getContactDto.getContactId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Contact contact = modelMapper.map(getContactDto, Contact.class);
        contact.setSecondUser(userService.getById(getContactDto.getContactId()));
        contactService.createOrUpdate(contact);
        PostContactDto postContactDto = modelMapper.map(contact , PostContactDto.class);
        postContactDto.setSecondUserId(contact.getSecondUser().getUserId());
        return new ResponseEntity<>( postContactDto , HttpStatus.OK);
    }
    @GetMapping("/get-contacts")
    public ResponseEntity<List<PostContactDto>> getContacts() {
        List<PostContactDto> pcdtl = new ArrayList<>();
        for (Contact con: getUserByToken().getContacts() ) {
            PostContactDto postContactDto = modelMapper.map(con , PostContactDto.class);
            postContactDto.setSecondUserId(con.getSecondUser().getUserId());
            pcdtl.add(postContactDto);
        }
        return new ResponseEntity<>(pcdtl, HttpStatus.OK);
    }
    @GetMapping("/get-contact")
    public ResponseEntity<PostContactDto> getContact(@RequestBody Long userId) throws Exception {
        if(!contactService.isInContact(getUserByToken() , userId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(getContactFromList(getUserByToken() , userId) , HttpStatus.OK);
    }
    public PostContactDto getContactFromList(User user , Long userId) throws Exception {
        for (Contact con: user.getContacts())
            if (con.getSecondUser().getUserId() == userId){
                PostContactDto postContactDto = modelMapper.map(con , PostContactDto.class);
                postContactDto.setSecondUserId(con.getSecondUser().getUserId());
                return postContactDto;
            }
        throw new Exception();
    }
}
