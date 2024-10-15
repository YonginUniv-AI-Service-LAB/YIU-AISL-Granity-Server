package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.dto.Request.BoardRequestDto;
import yiu.aisl.granity.dto.Request.OCRRequestDto;
import yiu.aisl.granity.service.OCRService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OCRController {
    private final OCRService ocrService;

    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity ocr(OCRRequestDto request) {
        String result = ocrService.callApi("POST", request, "pdf");
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
