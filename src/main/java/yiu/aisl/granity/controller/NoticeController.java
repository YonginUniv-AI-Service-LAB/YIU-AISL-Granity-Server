package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.dto.Request.NoticeRequestDto;
import yiu.aisl.granity.service.*;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    // 공지 및 뉴스 등록
    @PostMapping(value = "/notice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> postNotice(@AuthenticationPrincipal CustomUserDetails userDetails, NoticeRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(noticeService.postNotice(userDetails, request), HttpStatus.OK);
    }

    // 공지 및 뉴스 삭제
    @DeleteMapping(value = "/notice")
    public ResponseEntity<Boolean> deleteNotice(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer noticeId) throws Exception {
        return new ResponseEntity<Boolean>(noticeService.deleteNotice(userDetails, noticeId), HttpStatus.OK);
    }

    // 공지 및 뉴스 수정
    @PutMapping(value = "/notice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateNotice(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer noticeId, NoticeRequestDto request) throws Exception {
        return new ResponseEntity<>(noticeService.updateNotice(userDetails, noticeId, request), HttpStatus.OK);
    }

    // 뉴스 승인하기
    @PutMapping(value = "/manager/notice/news/approval")
    public ResponseEntity<Boolean> approvalNotice(@RequestParam(value = "id") Integer noticeId) throws Exception {
        return new ResponseEntity<>(noticeService.approvalNotice(noticeId), HttpStatus.OK);
    }

    // 뉴스 거절하기
    @PutMapping(value = "/manager/notice/news/rejection")
    public ResponseEntity<Boolean> rejectionNotice(@RequestParam(value = "id") Integer noticeId) throws Exception {
        return new ResponseEntity<>(noticeService.rejectionNotice(noticeId), HttpStatus.OK);
    }
}
