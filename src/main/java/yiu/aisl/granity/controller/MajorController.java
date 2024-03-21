package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.dto.MajorCurriculumRequestDto;
import yiu.aisl.granity.dto.MajorLabRequestDto;
import yiu.aisl.granity.dto.MajorMemberRegisterRequestDto;
import yiu.aisl.granity.security.CustomUserDetails;
import yiu.aisl.granity.service.MajorService;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    // 교수님 등록
    @PostMapping(value = "/manager/major/professor")
    public ResponseEntity<Boolean> registerProfessor(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MajorMemberRegisterRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerProfessor(userDetails, request), HttpStatus.OK);
    }

    // 교수님 수정
//    @PutMapping(value = "/manager/major/professor")
//    public ResponseEntity<Boolean> updateProfessor(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MajorMemberRegisterRequestDto request) {
//        return new ResponseEntity<Boolean>(majorService.updateProfessor(userDetails, request), HttpStatus.OK);
//    }

    // 교수님 삭제

    // 교수님 조회
    @GetMapping(value = "/major/professor")
    public ResponseEntity<Object> getProfessor(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(majorService.getProfessor(userDetails), headers, HttpStatus.OK);
    }

    // 학생회 등록
    @PostMapping(value = "/manager/major/council")
    public ResponseEntity<Boolean> registerCouncil(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MajorMemberRegisterRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerCouncil(userDetails, request), HttpStatus.OK);
    }

    // 학생회 수정

    // 학생회 삭제

    // 학생회 조회
    @GetMapping(value = "/major/council")
    public ResponseEntity<Object> getCouncil(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(majorService.getCouncil(userDetails), headers, HttpStatus.OK);
    }

    // 커리큘럼 등록
    @PostMapping(value = "/manager/major/curriculum")
    public ResponseEntity<Boolean> registerCurriculum(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MajorCurriculumRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerCurriculum(userDetails, request), HttpStatus.OK);
    }

    // 커리큘럼 수정

    // 커리큘럼 삭제

    // 커리큘럼 조회
    @GetMapping(value = "/major/curriculum")
    public ResponseEntity<Object> getCurriculum(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<Object>(majorService.getCurriculum(userDetails), headers, HttpStatus.OK);
    }

    // 연구실 등록
    @PostMapping(value = "/manager/major/lab")
    public ResponseEntity<Boolean> registerLab(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MajorLabRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerLab(userDetails, request), HttpStatus.OK);
    }

    // 연구실 수정

    // 연구실 삭제

    // 연구실 조회
    @GetMapping(value = "/major/lab")
    public ResponseEntity<Object> getLab(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<Object>(majorService.getLab(userDetails), headers, HttpStatus.OK);
    }

    // 학과 학생 조회

    // 학과 정보 수정

    // 학과 정보 수정
}
