package ir.mohaymen.iris.subscription;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class AddSubDto {
    @NotNull
    private Long chatId;
    private ArrayList<Long> userIds;
}
