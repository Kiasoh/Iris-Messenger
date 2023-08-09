package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Table(name = "contacts")
@Data
public class Contact {
    @ManyToOne
    @JoinColumn(name = "users" , referencedColumnName = "userName")
    private User userInterest;
    @ManyToOne
    @JoinColumn(name = "users" , referencedColumnName = "userName")
    private User userTarget;
    private String firstName;
    private String lastName;
}
