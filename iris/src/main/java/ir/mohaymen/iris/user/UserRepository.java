package ir.mohaymen.iris.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Iterable<User> findByFirstName(String firstName);

    Iterable<User> findByLastName(String firstName);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByUserName(String userName);

    void deleteByUserName(String UserName);

    void deleteByPhoneNumber(String phoneNumber);
}
