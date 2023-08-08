package ir.mohaymen.iris.reposetory;

import java.util.Optional;

import ir.mohaymen.iris.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByPhoneNumber(String phoneNumber);

}
