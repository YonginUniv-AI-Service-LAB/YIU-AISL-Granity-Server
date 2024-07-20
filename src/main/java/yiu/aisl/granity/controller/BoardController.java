package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.dto.Request.BoardRequestDto;
import yiu.aisl.granity.dto.Request.CommentRequestDto;
import yiu.aisl.granity.service.BoardService;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 등록
    @PostMapping(value = "/board", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> registerBoard(@AuthenticationPrincipal CustomUserDetails userDetails, BoardRequestDto request) throws Exception {
        return new ResponseEntity<>(boardService.registerBoard(userDetails, request), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping(value = "/board", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> deleteBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer boardId) throws Exception {
        return new ResponseEntity<>(boardService.deleteBoard(userDetails, boardId), HttpStatus.OK);
    }

    // 게시글 수정
//    @PutMapping(value = "/board", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<Boolean> updateBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer boardId, BoardRequestDto request) throws Exception {
//        return new ResponseEntity<>(boardService.updateBoard(userDetails, boardId, request), HttpStatus.OK);
//    }

    // 댓글 등록
    @PostMapping(value = "/board/comment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer boardId, CommentRequestDto request) throws Exception {
        return new ResponseEntity<>(boardService.registerComment(userDetails, boardId, request), HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping(value = "/board/comment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer commentId) throws Exception {
        return new ResponseEntity<>(boardService.deleteComment(userDetails, commentId), HttpStatus.OK);
    }

    // 댓글 수정
    @PutMapping(value = "/board/comment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> updateComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer commentId, CommentRequestDto request) throws Exception {
        return new ResponseEntity<>(boardService.updateComment(userDetails, commentId, request), HttpStatus.OK);
    }

    // 댓글 승인
    @PostMapping(value = "/manager/board/comment")
    public ResponseEntity<Boolean> approvalComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer commentId) throws Exception {
        return new ResponseEntity<>(boardService.approvalComment(userDetails, commentId), HttpStatus.OK);
    }

    // 댓글 승인 취소
    @PutMapping(value = "/manager/board/comment")
    public ResponseEntity<Boolean> rejectionComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer commentId) throws Exception {
        return new ResponseEntity<>(boardService.rejectionComment(userDetails, commentId), HttpStatus.OK);
    }
}
