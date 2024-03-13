package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.dto.LoginRequestDto;
import yiu.aisl.granity.dto.LoginResponseDto;
import yiu.aisl.granity.dto.RegisterRequestDto;
import yiu.aisl.granity.service.MainService;
import org.springframework.http.MediaType;

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
}
