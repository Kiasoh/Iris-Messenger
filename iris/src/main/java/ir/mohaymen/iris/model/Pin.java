package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table("pinnedmessages")
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id")
    private Message message;
    @ManyToOne
    @JoinColumn(name = "id")
    private Chat chat;
}
