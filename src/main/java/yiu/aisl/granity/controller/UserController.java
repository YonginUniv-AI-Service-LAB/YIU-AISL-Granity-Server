package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.dto.Request.UserRequestDto;
import yiu.aisl.granity.dto.Response.BoardResponseDto;
import yiu.aisl.granity.dto.Response.CommentResponseDto;
import yiu.aisl.granity.dto.Response.PushResponseDto;
import yiu.aisl.granity.dto.Response.UserResponseDto;
import yiu.aisl.granity.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 전체 알림 내역 조회
    @GetMapping(value = "/user/push")
    public ResponseEntity<List<PushResponseDto>> getMyPushList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return new ResponseEntity<>(userService.getMyPushList(userDetails), HttpStatus.OK);
    }

    // 특정 알림 확인
    @PutMapping(value = "/user/push")
    public ResponseEntity<Boolean> pushCheck(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer pushId) throws Exception {
        return new ResponseEntity<>(userService.pushCheck(userDetails, pushId), HttpStatus.OK);
    }

    // 내가 작성한 게시글 조회
    @GetMapping(value = "/user/board")
    public ResponseEntity<List<BoardResponseDto>> getMyBoardList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return new ResponseEntity<>(userService.getMyBoardList(userDetails), HttpStatus.OK);
    }

    // 내가 작성한 댓글 조회
    @GetMapping(value = "/user/comment")
    public ResponseEntity<List<CommentResponseDto>> getMyCommentList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return new ResponseEntity<>(userService.getMyCommentList(userDetails), HttpStatus.OK);
    }

    // 내 정보 조회
    @GetMapping(value = "/user")
    public ResponseEntity<UserResponseDto> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return new ResponseEntity<>(userService.getMyProfile(userDetails), HttpStatus.OK);
    }

    // 내 정보 수정
    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails, UserRequestDto request) throws Exception {
        return new ResponseEntity<>(userService.updateProfile(userDetails, request), HttpStatus.OK);
    }
}
