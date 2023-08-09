package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @NotBlank
    private String title;

    private String bio;

    @Column(unique = true)
    private String link;

    private boolean isPublic;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ChatType chatType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chat")
    public Set<Subscription> subs;
}
