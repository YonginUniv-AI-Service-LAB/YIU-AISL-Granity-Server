package yiu.aisl.granity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("refreshToken")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @JsonIgnore
    private String id;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
