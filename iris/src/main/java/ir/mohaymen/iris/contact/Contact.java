package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private User firstUser;

    @ManyToOne
    private User secondUser;

    @NotBlank
    private String firstName;

    private String lastName;
}
