package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "contacts")
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users fistUser;
    private Long secondUser;
    private String firstName;
    private String lastName;
}
