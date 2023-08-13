package ir.mohaymen.iris.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PageDto {
    Long chatId;
    int floor;
    int seal;
}
