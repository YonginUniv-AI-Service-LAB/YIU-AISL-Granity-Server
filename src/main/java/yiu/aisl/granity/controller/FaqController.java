package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.dto.FaqRequestDto;
import yiu.aisl.granity.service.FaqService;

@RestController
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    // FAQ 등록
    @PostMapping(value = "/manager/faq", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerFaq(FaqRequestDto request) throws Exception {
        return new ResponseEntity<>(faqService.registerFaq(request), HttpStatus.OK);
    }

    // FAQ 수정
    @PutMapping(value = "/manager/faq", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> updateFaq(@RequestParam(value = "id") Integer faqId, FaqRequestDto request) throws Exception {
        return new ResponseEntity<>(faqService.updateFaq(faqId, request), HttpStatus.OK);
    }

    // FAQ 삭제
    @DeleteMapping(value = "/manager/faq", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> deleteFaq(@RequestParam(value = "id") Integer faqId) throws Exception {
        return new ResponseEntity<>(faqService.deleteFaq(faqId), HttpStatus.OK);
    }
}
