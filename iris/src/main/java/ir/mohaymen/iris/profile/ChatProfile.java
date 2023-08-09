package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "chat_profiles")
public class ChatProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chatId")
    private Chat chat;
    @OneToOne
    @JoinColumn(name = "mediaId")
    private Media media;
    @NotBlank
    private Instant setAt;
}
