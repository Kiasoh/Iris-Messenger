package ir.mohaymen.iris.contact;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetContactDto {
    @NotNull
    private Long contactId;
    @NotNull
    private String firstName;
    private String lastName;
}
