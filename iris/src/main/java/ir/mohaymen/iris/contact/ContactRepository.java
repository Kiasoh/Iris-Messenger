package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByFirstUser(User firstUser);
    Iterable<Contact> findBySecondUser(User secondUser);
    @Query(value = """
        select case when count(c) > 0 THEN TRUE ELSE FALSE END
        from Contact c 
        where c.firstUser.userId = ?1 and c.secondUser.userId = ?2
""")
    boolean userInContact(Long firstUserId , Long secondUserId);
    @Query(value = """
        select  c 
        from Contact c 
        where c.firstUser.userId = ?1 and c.secondUser.userId = ?2
""")
    Contact getContact(Long firstUserId , Long secondUserId);
}
