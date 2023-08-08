package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chats")
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String bio;
    private String link;
    private boolean isPublic;
    @Enumerated(EnumType.ORDINAL)
    private ChatType chatType;
}


