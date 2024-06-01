package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.dto.FaqRequestDto;
import yiu.aisl.granity.security.CustomUserDetails;
import yiu.aisl.granity.service.FaqService;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    // FAQ 등록
    @PostMapping(value = "/manager/faq")
    public ResponseEntity<Boolean> registerFaq(@RequestBody FaqRequestDto request) {
        return new ResponseEntity<Boolean> (faqService.registerFaq(request), HttpStatus.OK);
    }

    // FAQ 조회
    @GetMapping(value = "/faq")
    public ResponseEntity<Object> getFaq(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(faqService.getFaq(userDetails), headers, HttpStatus.OK);
    }

    // FAQ 삭제
    @DeleteMapping(value = "/manager/faq")
    public ResponseEntity<Boolean> deleteFaq(@RequestParam(value = "id") Integer id) {
        return new ResponseEntity<Boolean>(faqService.deleteFaq(id), HttpStatus.OK);
    }

    // FAQ 수정
    @PutMapping(value = "/manager/faq")
    public ResponseEntity<Boolean> updateFaq(@RequestParam(value = "id") Integer id, @RequestBody FaqRequestDto request) {
        return new ResponseEntity<Boolean>(faqService.updateFaq(id, request), HttpStatus.OK);
    }
}
