package ir.mohaymen.iris.contact;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetContactDto {
    private Long contactId;
    private String firstName;
    private String lastName;
}
