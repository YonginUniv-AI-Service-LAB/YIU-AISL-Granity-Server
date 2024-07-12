package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 전체 알림 내역 조회

    // 특정 알림 확인
    @PutMapping(value = "/user/push")
    public ResponseEntity<Boolean> pushCheck(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer pushId) throws Exception {
        return new ResponseEntity<>(userService.pushCheck(userDetails, pushId), HttpStatus.OK);
    }
}
