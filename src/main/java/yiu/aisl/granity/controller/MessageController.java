package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.dto.Request.MessageRequestDto;
import yiu.aisl.granity.dto.Response.MessageResponseDto;
import yiu.aisl.granity.service.MessageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    // 쪽지 조회
    @GetMapping(value = "/manager/message")
    public ResponseEntity<List<MessageResponseDto>> getMessages(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return new ResponseEntity<>(messageService.getMessages(userDetails), HttpStatus.OK);
    }

    // 쪽지 보내기
    @PostMapping(value = "/manager/message", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> sendMessage(@AuthenticationPrincipal CustomUserDetails userDetails, MessageRequestDto request) throws Exception {
        return new ResponseEntity<>(messageService.sendMessage(userDetails, request), HttpStatus.OK);
    }
}
