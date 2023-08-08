package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "messages")
@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @JoinColumn(name = "id")
    @ManyToOne
    private Chat originChat;
    @JoinColumn(name = "id")
    @ManyToOne
    private User sender;
    @JoinColumn(name = "id")
    @OneToOne
    private Media media;
    private Timestamp sendAt;
    private Timestamp editedAt;
}
