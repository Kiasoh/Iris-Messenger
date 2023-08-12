package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Iterable<Contact> findByFirstUser(User firstUser);

    Iterable<Contact> findBySecondUser(User secondUser);
}
