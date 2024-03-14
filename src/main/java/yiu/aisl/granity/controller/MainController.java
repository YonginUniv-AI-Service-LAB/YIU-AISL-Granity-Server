package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.dto.*;
import yiu.aisl.granity.service.MainService;
import org.springframework.http.MediaType;

import javax.print.attribute.standard.Media;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    // 회원가입
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> register(RegisterRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(mainService.register(request), HttpStatus.OK);
    }

    // 로그인
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto request) throws Exception {
        return new ResponseEntity<LoginResponseDto>(mainService.login(request), HttpStatus.OK);
    }

    // 인증코드 전송(회원가입)
    @PostMapping(value = "/join/email", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> email(EmailRequestDto request) throws Exception {
        return new ResponseEntity<String>(mainService.joinEmail(request.getId()), HttpStatus.OK);
    }

    // 인증코드 전송(비밀번호 변경)
    @PostMapping(value = "/pwd/email", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> pwdEmail(EmailRequestDto request) throws Exception {
        return new ResponseEntity<String>(mainService.pwdEmail(request.getId()), HttpStatus.OK);
    }

    // accessToken 재발급
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
        return new ResponseEntity<>( mainService.tokenRefresh(token), HttpStatus.OK);
    }

    // accessToken 재발급
//    @PostMapping(value = "/token/refresh", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<TokenDto> refresh(TokenDto token) throws Exception {
//        return new ResponseEntity<>(mainService.tokenRefresh(token), HttpStatus.OK);
//    }
}
