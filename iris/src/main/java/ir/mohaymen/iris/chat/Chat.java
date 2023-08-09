package ir.mohaymen.iris.chat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chats")
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String title;
    private String bio;
    @Column(unique = true)
    private String link;
    private boolean isPublic;
    @Enumerated(EnumType.ORDINAL)
    private ChatType chatType;
}
