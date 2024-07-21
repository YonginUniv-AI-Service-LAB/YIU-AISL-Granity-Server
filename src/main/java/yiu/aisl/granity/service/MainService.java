package yiu.aisl.granity.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.Token;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.domain.UserMajor;
import yiu.aisl.granity.dto.*;
import yiu.aisl.granity.dto.Request.UserRequestDto;
import yiu.aisl.granity.dto.Request.UserResponseDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;
import yiu.aisl.granity.security.JwtProvider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;
    private final UserMajorRepository userMajorRepository;

    private final JavaMailSender javaMailSender;
    private static int number;
    private static String authNum;

    private long exp_refreshToken = Duration.ofDays(14).toMillis(); // 만료시간 2주

    // [API] 회원가입
    public Boolean register(UserRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getId().isEmpty() || request.getPwd().isEmpty() || request.getName().isEmpty() ||
        request.getGrade() == null || request.getRole() == null || request.getStatus() == null || request.getMajor() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // 데이터 중복(id) - 409
        if(userRepository.existsById(request.getId())) {
            throw new CustomException(ErrorCode.DUPLICATE);
        }

        try {
            User user = User.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .grade(request.getGrade())
                    .pwd(passwordEncoder.encode(request.getPwd()))
                    .role(request.getRole())
                    .status(request.getStatus())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .push(0)
                    .build();

            userRepository.save(user);
            UserMajor userMajor = UserMajor.builder()
                    .user(user)
                    .major(request.getMajor())
                    .build();

            userMajorRepository.save(userMajor);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 로그인
    public UserResponseDto login(UserRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getId().isEmpty() || request.getPwd().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // 회원없음 - 404
        User user = userRepository.findById(request.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        // 회원정보 불일치(id, pwd) - 401
        if(!passwordEncoder.matches(request.getPwd(), user.getPwd())) {
            throw new CustomException(ErrorCode.USER_DATA_INCONSISTENCY);
        }

        try {
            String accessToken = jwtProvider.createToken(user);
            user.setRefreshToken(createRefreshToken(user));
            UserResponseDto response = UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .grade(user.getGrade())
                    .status(user.getStatus())
                    .push(user.getPush())
                    .role(user.getRole())
                    .token(TokenDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(user.getRefreshToken())
                            .build())
                    .build();
            System.out.println("로그인 할 때 발급 받은 accessToken 확인 : " +accessToken);
            return response;
        } catch(Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // [API] 회원가입 시 이메일 전송
    public String sendEmail(String id) throws MessagingException, UnsupportedEncodingException {
        // id 미입력 - 400
        if(id == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 회원 중복 - 409
        if (userRepository.findById(id).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE);

        // 메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(id+"@yiu.ac.kr");
        // 실제 메일 전송
        javaMailSender.send(emailForm);

        return authNum; // 인증 코드 반환
    }

    // [API] 비밀번호 재설정 시 이메일 전송
    public String sendEmailWhenPwdChanges(String id) throws MessagingException, UnsupportedEncodingException {
        // id 미입력 - 400
        if(id == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 회원 없음 - 404
        User user = userRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        // 메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(id+"@yiu.ac.kr");
        // 실제 메일 전송
        javaMailSender.send(emailForm);

        return authNum; // 인증 코드 반환
    }

    // [API] 비밀번호 변경
    public Boolean changePwd(UserRequestDto request) throws Exception {
        // 데이버 미입력 - 400
        if(request.getId().isEmpty() || request.getPwd().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // 회원 없음 - 404
        User user = userRepository.findById(request.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));
        try {
            user.setPwd(passwordEncoder.encode(request.getPwd()));
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] accessToken 재발급
    public TokenDto reIssuanceAccessToken(TokenDto token) throws Exception {
        String id = null;
        try {
            id = jwtProvider.getId(token.getAccessToken());
            System.out.println("id 확인: " + id);
        } catch (ExpiredJwtException e) {
            id = e.getClaims().get("id", String.class);
        }

        User user = userRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        Token refreshToken = validRefreshToken(user, token.getRefreshToken());

        try {
            if (refreshToken != null) {
                return TokenDto.builder()
                        .accessToken(jwtProvider.createToken(user))
                        .refreshToken(refreshToken.getRefreshToken())
                        .build();
            } else {
                throw new CustomException(ErrorCode.LOGIN_REQUIRED);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(user.getId())
                        .refreshToken(UUID.randomUUID().toString())
                        .expiration(exp_refreshToken)
                        .build()
        );
        return token.getRefreshToken();
    }

    public Token validRefreshToken(User user, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_REQUIRED));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefreshToken() == null) throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        try {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
            if (token.getExpiration() < 10) {
                token.setExpiration(1000L);
                tokenRepository.save(token);
            }
            // 토큰이 같은지 비교
            if (!token.getRefreshToken().equals(refreshToken)) {
                // 원래 null
                throw new CustomException(ErrorCode.LOGIN_REQUIRED);
            } else {
                return token;
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static void createNumber() {
        // (int) Math.random() * (최댓값-최소값+1) + 최소값
        number = (int)(Math.random() * (90000)) + 100000;
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        // 코드를 생성합니다.
        createCode();
        String setFrom = "yiuaiservicelab@gmail.com";	// 보내는 사람
        String toEmail = email;		// 받는 사람(값 받아옵니다.)
        String title = "YMate 회원가입 인증번호";		// 메일 제목

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
        message.setSubject(title);		// 제목 설정

        // 메일 내용 설정
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 용인대학교 YMate 입니다. </h1>";
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

        message.setFrom(setFrom);		// 보내는 사람 설정
        // 위 String으로 받은 내용을 아래에 넣어 내용을 설정합니다.
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }

    // 인증번호 6자리 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<6; i++)
            key.append(random.nextInt(9));

        authNum = key.toString();
    }
}
