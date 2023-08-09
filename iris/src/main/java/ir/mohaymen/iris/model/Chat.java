package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "chats")
@Data
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


