package ir.mohaymen.iris.user;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Iterable<User> findByFirstName(String firstName);

    Iterable<User> findByLastName(String firstName);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByUserName(String userName);

    boolean existsByPhoneNumber(String phoneNumber);

    void deleteByUserName(String UserName);

    void deleteByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update User u
            set u.lastSeen =:updateTime
            where u.userId=:userId
            """)
    void updateLastSeen(@Param("userId") Long userId, @Param("updateTime") Instant updateTime);
}
