package yiu.aisl.granity.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.service.JpaUserDetailsService;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private final JwtProperties jwtProperties;
    @Value("${jwt.secret.key}")
    private String salt;
    private Key secretKey;

    // accessToken 만료 시간 : 30분으로 설정
    private long accessTokenValidTime = Duration.ofMinutes(30).toMillis();

    // refreshToken 만료 시간 : 14일로 설정
    private long refreshTokenValidTime = Duration.ofDays(14).toMillis();

    private final JpaUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    // token 생성
    public String createToken(User user) {
        Date now = new Date();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .setSubject(user.getId())
                .claim("id", user.getId())
                .signWith(secretKey, SignatureAlgorithm.HS256);

        // 개발자에게 ADMIN (관리자 권한 부여)
        if (user.getRole().equals(0)) {
            jwtBuilder.claim("role", "ADMIN");
            System.out.println("사용자 역할: " + user.getRole());
            System.out.println("관리자 권한이 부여됨");
        }
        // 교수와 조교 MANAGER (매니저 권한 부여)
        else if (user.getRole().equals(2) || user.getRole().equals(3)) {
            jwtBuilder.claim("role", "MANAGER");
            System.out.println("매니저 권한이 부여됨");
        }
        // 학생 USER (사용자 권한 부여)
        else {
            jwtBuilder.claim("role", "USER");
            System.out.println("사용자 권한이 부여됨");
        }
        return jwtBuilder.compact();
    }

    // 권한 정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        String userId = this.getId(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        System.out.println("사용자 ID: " +userId);
        System.out.println("사용자 인증 정보: " +userDetails);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 권한 획득
    public String getId(String token) {
        System.out.println(token);
        // 만료된 토큰에 대해 parseClaimsJws를 수행하면 io.jsonwebtoken.ExpiredJwtException이 발생한다.
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return e.getClaims().getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Authorization Header 를 통해 인증
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if(!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token);

            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("복호화 에러: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 정보 리턴
    public TokenInfo getTokenInfo(String token) {
        Claims body = getClaims(token);
        Set<String> keySet = body.keySet();

        String id = body.get("id", String.class);
        Date issuedAt = body.getIssuedAt();
        Date expiration = body.getExpiration();
        return new TokenInfo(id, issuedAt, expiration);
    }

    @Getter
    public class TokenInfo {
        private String id;
        private Date issuedAt;
        private Date expire;

        public TokenInfo(String id, Date issuedAt, Date expire) {
            this.id = id;
            this.issuedAt = issuedAt;
            this.expire = expire;
        }
    }
}
