package yiu.aisl.granity.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender emailSender;

    private long exp_refreshToken = Duration.ofDays(14).toMillis();
    private String authNum;

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
                    .pwd(passwordEncoder.encode(request.getPwd()))
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
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findById(request.getId()).orElseThrow();
        user.setRefreshToken(createRefreshToken(user));
        return LoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .token(TokenDto.builder()
                        .accessToken(jwtProvider.createToken(user))
                        .refreshToken(user.getRefreshToken())
                        .build())
                .build();
    }

    // [API] accessToken 재발급
    public TokenDto tokenRefresh(TokenDto token) throws Exception {
        String id = jwtProvider.getId(token.getAccessToken());
        User user = userRepository.findById(id).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        Token refreshToken = validRefreshToken(user, token.getRefreshToken());

        if (refreshToken != null) {
            return TokenDto.builder()
                    .accessToken(jwtProvider.createToken(user))
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();
        } else {
            throw new IllegalArgumentException("로그인을 해주세요");
        }
    }

    // [API] 회원가입 시 인증 메일 전송
    public String joinEmail(String id) throws  MessagingException {
        id = id+"@yiu.ac.kr";
        MimeMessage emailForm = createEmailForm(id);
        emailSender.send(emailForm);

        return authNum;
    }

    // [API] 비밀번호 변경 시 인증 메일 전송
    public String pwdEmail(String id) throws  MessagingException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new BadCredentialsException("해당 아이디의 회원이 존재하지 않습니다."));
        id = id+"@yiu.ac.kr";
        MimeMessage emailForm = createEmailForm(id);
        emailSender.send(emailForm);

        return authNum;
    }

    // [API] 비밀번호 변경
    public boolean pwdChange(String id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() ->
                new BadCredentialsException("해당 아이디의 회원이 존재하지 않습니다."));

        LocalDateTime updatedAt = LocalDateTime.now();
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        user.setUpdatedAt(updatedAt);

        userRepository.save(user);
        return true;
    }

    public Token validRefreshToken(User user, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefreshToken() == null) {
            return null;
        } else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장..?
            if (token.getExpiration() < 10) {
                token.setExpiration(1000);
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if (!token.getRefreshToken().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
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

    public void createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for(int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            key.append(digit);
        }
        authNum = key.toString();
    }

    public MimeMessage createEmailForm(String email) throws MessagingException {
        // 코드 생성
        createCode();
        String setFrom = "callikys@naver.com";
        String toEmail = email;
        String title = "YDRIVE 이메일 인증";

        jakarta.mail.internet.MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);

        // 메일 내용 설정
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 YDRIVE 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgOfEmail += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgOfEmail += "<div style='font-size:130%'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authNum + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);
        message.setText(msgOfEmail, "utf-8", "html");
        return message;
    }
}
