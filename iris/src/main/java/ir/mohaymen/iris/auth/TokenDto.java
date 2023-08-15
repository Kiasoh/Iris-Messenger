package ir.mohaymen.iris.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    @JsonProperty("access_token")
    protected String accessToken;
    @JsonProperty("refresh_token")
    protected String refreshToken;
}
