package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.dto.MajorRequestDto;
import yiu.aisl.granity.service.MajorService;

@RestController
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    // 학과 정보 등록
    @PostMapping(value = "/manager/major", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerMajor(MajorRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerMajor(request), HttpStatus.OK);
    }

    // 학과 정보 수정
    @PutMapping(value = "/manager/major", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> updateMajor(@RequestParam(value = "id") Integer majorId, MajorRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.updateMajor(majorId, request), HttpStatus.OK);
    }
}
