package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @JoinColumn(name = "reply_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Message repliedMessage;

    @Column(columnDefinition = "TEXT")
    private String text;

    @JoinColumn(name = "chatId")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Chat chat;

    @JoinColumn(name = "origin_message_id")
    @ManyToOne
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Message originMessage;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User sender;

    @JoinColumn(name = "mediaId")
    @OneToOne
    private Media media;

    @NotNull
    private Instant sendAt;

    private Instant editedAt;
}
