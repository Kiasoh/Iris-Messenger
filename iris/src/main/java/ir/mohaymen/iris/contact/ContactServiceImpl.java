package ir.mohaymen.iris.contact;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactServiceImpl {

    private ContactRepository contactRepository;
}
