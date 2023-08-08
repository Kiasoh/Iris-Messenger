package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;


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
    private Timestamp sendAt;
    private Timestamp editedAt;
}
