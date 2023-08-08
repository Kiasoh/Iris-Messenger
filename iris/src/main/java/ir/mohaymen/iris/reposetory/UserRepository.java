package ir.mohaymen.iris.reposetory;

import java.util.Optional;

import ir.mohaymen.iris.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

  Optional<Users> findByPhoneNumber(String phoneNumber);

}
