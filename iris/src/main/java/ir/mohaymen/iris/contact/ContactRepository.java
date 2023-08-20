package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
        where c.firstUser.userId = :firstUserId and c.secondUser.userId = :secondUserId
""")
    boolean userInContact(@Param("firstUserId") Long firstUserId ,@Param("secondUserId") Long secondUserId);
    @Query(value = """
        select  c
        from Contact c
        where c.firstUser.userId = :firstUserId and c.secondUser.userId = :secondUserId
""")
    Contact getContact(@Param("firstUserId") Long firstUserId ,@Param("secondUserId") Long secondUserId);
}
