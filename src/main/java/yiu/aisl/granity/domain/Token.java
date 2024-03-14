package yiu.aisl.granity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@RedisHash("refreshToken")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @JsonIgnore
    @Column(unique = true)
    @NotNull
    private String id;

    private String refreshToken;
    private String accessToken;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Integer expiration;

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }
}
