package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private String text;
    @JoinColumn(name = "chatId")
    @ManyToOne
    private Chat originChat;
    @JoinColumn(name = "userId")
    @ManyToOne
    private User sender;
    @JoinColumn(name = "mediaId")
    @OneToOne
    private Media media;
    @NotBlank
    private Instant sendAt;
    private Instant editedAt;
}
