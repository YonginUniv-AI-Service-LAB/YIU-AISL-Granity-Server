package yiu.aisl.granity.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JwtProperties {
    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret.key}")
    private String secretKey;
}
