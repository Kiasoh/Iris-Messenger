package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contacts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"first_user_id", "second_user_id"})})
@Getter
@Setter
public class Contact implements Nameable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_user_id")
    private User firstUser;

    @ManyToOne
    @JoinColumn(name = "second_user_id")
    private User secondUser;

    @NotBlank
    private String firstName;

    private String lastName;
    @Override
    public String fullName() {
        String name = firstName;
        if(lastName!=null)
            name =name + " " + lastName;
        return name;
    }
}
