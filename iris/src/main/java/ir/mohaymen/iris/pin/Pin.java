package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.chat.Chat;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pinned_messages")
@Data
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pinId;
    @ManyToOne
    @JoinColumn(name = "messageId")
    private Message message;
    @ManyToOne
    @JoinColumn(name = "chatId")
    private Chat chat;
}
