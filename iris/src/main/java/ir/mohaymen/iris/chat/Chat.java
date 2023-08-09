package ir.mohaymen.iris.chat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    @NotBlank
    private String title;
    private String bio;
    @Column(unique = true)
    private String link;
    private boolean isPublic;
    @Enumerated(EnumType.ORDINAL)
    @NotBlank
    private ChatType chatType;

}
