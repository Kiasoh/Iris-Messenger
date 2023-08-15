package ir.mohaymen.iris.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PageDto {
    private Long chatId;
    private Integer floor;
    private Integer sill;
}
