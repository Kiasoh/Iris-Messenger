package ir.mohaymen.iris.subscription;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class AddSubDto {

    private Long chatId;
    private ArrayList<Long> userIds;
}
