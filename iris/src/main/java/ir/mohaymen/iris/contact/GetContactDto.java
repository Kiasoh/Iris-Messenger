package ir.mohaymen.iris.contact;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetContactDto {
    private Long contactId;
    private String firstName;
    private String lastName;
}
