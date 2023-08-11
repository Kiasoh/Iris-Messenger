package ir.mohaymen.iris.user;

import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String userName;

    private String bio;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "firstUser")
    private Set<Contact> contacts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public String getPassword() {
        // "password" encoded using bcrypt
        return "$2a$10$BfAHeaJGH9qGshWKnZwHfOugl9cwf4zd5/GMTPtXTLJqSRte5pj.S";
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
