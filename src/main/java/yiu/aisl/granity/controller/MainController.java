package yiu.aisl.granity.controller;

import jakarta.mail.MessagingException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.dto.*;
import yiu.aisl.granity.dto.Request.UserRequestDto;
import yiu.aisl.granity.dto.Response.UserResponseDto;
import yiu.aisl.granity.service.MainService;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    // 회원가입
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> join(UserRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(mainService.register(request), HttpStatus.OK);
    }

    // 로그인
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<LoginDto> login(UserRequestDto request) throws Exception {
        return new ResponseEntity<LoginDto>(mainService.login(request), HttpStatus.OK);
    }

    // 회원가입 시 이메일 전송
    @PostMapping(value = "/register/email", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> sendEmail(UserRequestDto request) throws MessagingException, UnsupportedEncodingException {
        return new ResponseEntity<String>(mainService.sendEmail(request.getId()), HttpStatus.OK);
    }

    // 비밀번호 변경 시 이메일 전송
    @PostMapping(value = "/pwd/email", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> changePwdEmail(UserRequestDto request) throws MessagingException, UnsupportedEncodingException {
        return new ResponseEntity<String>(mainService.sendEmailWhenPwdChanges(request.getId()), HttpStatus.OK);
    }

    // 비밀번호 변경
    @PostMapping(value = "/pwd/change", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> changePwd(UserRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(mainService.changePwd(request), HttpStatus.OK);
    }

    // accessToken 재발급
    @PostMapping(value = "/token/refresh", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TokenDto> reIssuanceAccessToken(TokenDto request) throws Exception {
        return new ResponseEntity<TokenDto>(mainService.reIssuanceAccessToken(request), HttpStatus.OK);
    }
}
