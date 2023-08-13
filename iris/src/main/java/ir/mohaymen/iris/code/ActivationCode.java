package ir.mohaymen.iris.code;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Document
@Builder
public class ActivationCode {
    @Id
    @Indexed
    private Long id;
    @Indexed
    private String code;
    @Indexed
    private String phoneNumber;
}
