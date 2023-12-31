package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pinned_messages")
@Getter
@Setter
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pinId;

    @OneToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
