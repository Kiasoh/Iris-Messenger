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
    private User fistUser;
    @ManyToOne
    private User secondUser;
    private String firstName;
    private String lastName;
}
