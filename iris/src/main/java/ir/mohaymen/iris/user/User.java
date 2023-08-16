package ir.mohaymen.iris.user;

import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.profile.UserProfile;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.token.Token;
import ir.mohaymen.iris.utility.Nameable;
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
public class User implements UserDetails, Nameable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Subscription> subs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserProfile> profiles;

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

    public String getUserName(){
        return userName;
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
