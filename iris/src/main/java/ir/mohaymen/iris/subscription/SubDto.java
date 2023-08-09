package ir.mohaymen.iris.subscription;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SubDto {
    private Long chatId;
    private ArrayList<Long> userIds;
}
