package ir.mohaymen.iris.model;

import jakarta.persistence.*;

import java.time.Instant;

public class ChatProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id")
    private Chat chat;
    @OneToOne
    @JoinColumn(name = "id")
    private Media media;
    private Instant setAt;
}
