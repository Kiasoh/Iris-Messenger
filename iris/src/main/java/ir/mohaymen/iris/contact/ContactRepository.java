package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Iterable<Contact> findByFirstUser(User firstUser);

    Iterable<Contact> findBySecondUser(User secondUser);
}
