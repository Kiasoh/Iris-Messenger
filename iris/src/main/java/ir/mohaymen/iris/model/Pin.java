package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pinnedmessages")
@Data
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pinId;
    @ManyToOne
    @JoinColumn(name = "messageId" )
    private Message message;
    @ManyToOne
    @JoinColumn(name = "chatId")
    private Chat chat;
}
