package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.dto.*;
import yiu.aisl.granity.service.MainService;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    // 회원가입
    @PostMapping(value = "/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(mainService.register(request), HttpStatus.OK);
    }

    // 로그인
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) throws Exception {
        return new ResponseEntity<LoginResponseDto>(mainService.login(request), HttpStatus.OK);
    }

    // 인증코드 전송(회원가입)
    @PostMapping(value = "/register/email")
    public ResponseEntity<String> email(@RequestBody EmailRequestDto request) throws Exception {
        return new ResponseEntity<String>(mainService.joinEmail(request.getId()), HttpStatus.OK);
    }

    // 인증코드 전송(비밀번호 변경)
    @PostMapping(value = "/pwd/email")
    public ResponseEntity<String> pwdEmail(@RequestBody EmailRequestDto request) throws Exception {
        return new ResponseEntity<String>(mainService.pwdEmail(request.getId()), HttpStatus.OK);
    }

    // 비밀번호 변경
    @PostMapping(value = "/pwd/change")
    public ResponseEntity<Boolean> pwdChange(@RequestBody PwdChangeRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(mainService.pwdChange(request), HttpStatus.OK);
    }

    // accessToken 재발급
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
        return new ResponseEntity<>( mainService.tokenRefresh(token), HttpStatus.OK);
    }
}
