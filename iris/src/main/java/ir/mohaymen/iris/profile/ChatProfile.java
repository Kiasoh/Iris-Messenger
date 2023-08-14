package ir.mohaymen.iris.profile;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.media.Media;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "chat_profiles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @NotNull
    private Instant setAt;
}
