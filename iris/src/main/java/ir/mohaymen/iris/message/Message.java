package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

import ir.mohaymen.iris.user.User;

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
    private Instant sendAt;
    private Instant editedAt;
}
