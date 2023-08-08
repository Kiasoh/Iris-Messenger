package ir.mohaymen.iris.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Table(name = "contacts")
@Data
public class Contact {
    @ManyToOne
    @JoinColumn(name = "users" , referencedColumnName = "userName")
    private Users userInterest;
    @ManyToOne
    @JoinColumn(name = "users" , referencedColumnName = "userName")
    private Users userTarget;
    private String firstName;
    private String lastName;
}
