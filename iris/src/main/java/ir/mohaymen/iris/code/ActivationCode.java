package ir.mohaymen.iris.code;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Document(timeToLive = 120L)
@Data
@Builder
public class ActivationCode {
    @Id
    @Indexed
    private Long id;
    @Indexed
    @Column(unique = true)
    private String code;
    @Indexed
    private String phoneNumber;
}
