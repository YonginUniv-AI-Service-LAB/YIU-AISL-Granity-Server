package yiu.aisl.granity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.service.JpaUserDetailsService;

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

    private long accessTokenValidTime = Duration.ofMinutes(30).toMillis(); // 만료시간 30분
    private long refreshTokenValidTime = Duration.ofDays(14).toMillis(); // 만료시간 2주

    private final JpaUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createToken(User user) {
        Date now = new Date();

        System.out.println(new Date(System.currentTimeMillis()));

        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .setSubject(user.getId())
                .claim("id", user.getId())
                .signWith(secretKey, SignatureAlgorithm.HS256);

        System.out.println("만료 시간: " +new Date(System.currentTimeMillis() + accessTokenValidTime));

        if (user.getRole() == 0) {
            jwtBuilder.claim("role", "ADMIN");
            System.out.println("사용자 역할: " + user.getRole());
            System.out.println("관리자 권한이 부여됨");
        } else if (user.getRole()==2 || user.getRole() == 3) {
            jwtBuilder.claim("role", "MANAGER");
            System.out.println("매니저 권한이 부여됨");
        } else if(user.getRole() == 1) {
            jwtBuilder.claim("role", "USER");
            System.out.println("유저(학생) 권한 부여");
        }
        return jwtBuilder.compact();
    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        String userId = this.getId(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        System.out.println("사용자 ID: " +userId);
        System.out.println("사용자 인증 정보: " +userDetails);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 account 획득
    public String getId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", String.class);
    }

    // Authorization Header를 통해 인증을 한다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
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
        if (token == null || token.trim().isEmpty()) {
            System.out.println("JWT 문자열이 null이거나 비어 있습니다.");
            return false;
        }
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Date now = new Date();
            Date expiration = claims.getBody().getExpiration();
            System.out.println("현재 시간: " + now);
            System.out.println("만료 시간: " + expiration);

            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 만료: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 문자열 오류: " + e.getMessage());
            return false;
        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            System.out.println("복호화 에러: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
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