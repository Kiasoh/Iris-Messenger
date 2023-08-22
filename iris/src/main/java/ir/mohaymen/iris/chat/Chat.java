package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.profile.ChatProfile;
import ir.mohaymen.iris.subscription.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private String title;

    private String bio;

    @Column(unique = true)
    private String link;

    private boolean isPublic;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ChatType chatType;

    @NotNull
    private Instant createdAt;
}
