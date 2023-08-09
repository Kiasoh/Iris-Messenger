package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Table(name = "chats")
@Data
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

    @OneToMany
    public ArrayList<Subscription> subs;
}
