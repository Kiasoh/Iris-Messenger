package ir.mohaymen.iris.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends CrudRepository<Token, Integer> {

  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.userId = u.userId\s
      where u.userId = :phoneNumber and t.expiresAt >= current_timestamp\s
      """)
  List<Token> findAllValidTokenByUserPhoneNumber(@Param("phoneNumber") String phoneNumber);

  Optional<Token> findByToken(String token);

  @Query("""
  delete
  from Token t
  where t.expiresAt <current_timestamp
""")
  void deleteAllExpiredTokens();
}
