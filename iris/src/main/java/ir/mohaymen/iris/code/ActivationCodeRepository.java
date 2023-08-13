package ir.mohaymen.iris.code;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ActivationCodeRepository extends RedisDocumentRepository<ActivationCode,Long> {
    Optional<ActivationCode> findByCode(String code);
    Optional<ActivationCode> findByPhoneNumber(String phoneNumber);
    Iterable<ActivationCode> findAllByPhoneNumber(String phoneNumber);

//    void deleteByPhoneNumber(String phoneNumber);

//    void deleteAllByPhoneNumber(String phoneNumber);
}
