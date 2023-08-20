package ir.mohaymen.iris.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CreateChatDto {
    @NotBlank
    private String title;
    private String bio;
    private String link;
    @NotNull
    private boolean isPublic;
    @NotNull
    private ChatType chatType;
    private ArrayList<Long> userIds;
}
