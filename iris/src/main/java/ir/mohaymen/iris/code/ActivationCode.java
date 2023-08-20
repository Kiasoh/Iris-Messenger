package ir.mohaymen.iris.code;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivationCode {
    private String code;
    private String phoneNumber;
}
