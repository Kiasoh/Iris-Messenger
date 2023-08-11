package ir.mohaymen.iris.code;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Optional;
import java.util.function.Function;

public interface ActivationCodeRepository extends RedisDocumentRepository<ActivationCode,Long> {
    Optional<ActivationCode> findByCode(String code);
    void deleteByPhoneNumber(String phoneNumber);
}
