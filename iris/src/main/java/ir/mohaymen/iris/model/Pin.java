package ir.mohaymen.iris.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pinnedmessages")
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
