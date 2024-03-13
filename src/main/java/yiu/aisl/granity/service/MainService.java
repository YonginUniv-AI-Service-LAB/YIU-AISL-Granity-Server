package yiu.aisl.granity.service;

import lombok.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import yiu.aisl.granity.dto.TokenDto;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.domain.Token;
import yiu.aisl.granity.dto.LoginRequestDto;
import yiu.aisl.granity.dto.LoginResponseDto;
import yiu.aisl.granity.dto.RegisterRequestDto;
import yiu.aisl.granity.repository.TokenRepository;
import yiu.aisl.granity.repository.UserRepository;
import yiu.aisl.granity.security.JwtProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    private long exp_refreshToken = Duration.ofDays(14).toMillis();

    // [API] 회원가입
    public boolean register(RegisterRequestDto request) {
        int major_id2 = Optional.ofNullable(request.getMajor_id2()).orElse(0); // 복수전공 or 부전공
        int major_id3 = Optional.ofNullable(request.getMajor_id2()).orElse(0); // 복수전공 or 부전공
        try {
            LocalDateTime createdAt = LocalDateTime.now();
            User user = User.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .major_id1(request.getMajor_id1())
                    .major_id2(major_id2)
                    .major_id3(major_id3)
                    .grade(request.getGrade())
                    .pwd(request.getPwd())
                    .role(request.getRole())
                    .status(request.getStatus())
                    .createdAt(createdAt)
                    .build();

            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }

    // [API] 로그인
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findById(request.getId()).orElseThrow();
        user.setRefreshToken(createRefreshToken(user));
        return LoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .major_id1(user.getMajor_id1())
                .major_id2(user.getMajor_id2())
                .major_id3(user.getMajor_id3())
                .grade(user.getGrade())
                .role(user.getRole())
                .status(user.getStatus())
                .token(TokenDto.builder()
                        .accessToken(jwtProvider.createToken(user.getId()))
                        .refreshToken(user.getRefreshToken())
                        .build())
                .build();
    }

    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(user.getId())
                        .refreshToken(UUID.randomUUID().toString())
                        .expiration(300)
                        .build()
        );
        return token.getRefreshToken();
    }
}
