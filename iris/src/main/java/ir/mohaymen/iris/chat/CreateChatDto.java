package ir.mohaymen.iris.chat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CreateChatDto {
    private String title;
    private String bio;
    private String link;
    private boolean isPublic;
    @NotNull
    private ChatType chatType;
    private Set<Long> userIds;
}
