package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@Getter
@Setter
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
