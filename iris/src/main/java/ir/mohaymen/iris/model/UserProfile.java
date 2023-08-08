package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id")
    private User user;
    @OneToOne
    @JoinColumn(name = "id")
    private Media media;
    private Instant setAt;
}
