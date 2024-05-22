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
    public ResponseEntity<Boolean> registerProfessor(@RequestBody MajorMemberRegisterRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerProfessor(request), HttpStatus.OK);
    }

    // 교수님 수정
    @PutMapping(value = "/manager/major/professor")
    public ResponseEntity<Boolean> updateProfessor(@RequestParam(value = "id") Integer id,  @RequestBody MajorMemberRegisterRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.updateProfessor(id, request), HttpStatus.OK);
    }

    // 교수님 삭제
    @DeleteMapping(value = "/manager/major/professor")
    public ResponseEntity<Boolean> deleteProfessor(@RequestParam(value = "id") Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<Boolean>(majorService.deleteProfessor(id), HttpStatus.OK);
    }

    // 교수님 조회
    @GetMapping(value = "/major/professor")
    public ResponseEntity<Object> getProfessor(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(majorService.getProfessor(userDetails), headers, HttpStatus.OK);
    }

    // 학생회 등록
    @PostMapping(value = "/manager/major/council")
    public ResponseEntity<Boolean> registerCouncil(@RequestBody MajorMemberRegisterRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerCouncil(request), HttpStatus.OK);
    }

    // 학생회 수정
    @PutMapping(value = "/manager/major/council")
    public ResponseEntity<Boolean> updateCouncil(@RequestParam(value = "id") Integer id,  @RequestBody MajorMemberRegisterRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.updateCouncil(id, request), HttpStatus.OK);
    }

    // 학생회 삭제
    @DeleteMapping(value = "/manager/major/council")
    public ResponseEntity<Boolean> deleteCouncil(@RequestParam(value = "id") Integer id) {
        return new ResponseEntity<>(majorService.deleteCouncil(id), HttpStatus.OK);
    }

    // 학생회 조회
    @GetMapping(value = "/major/council")
    public ResponseEntity<Object> getCouncil(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(majorService.getCouncil(userDetails), headers, HttpStatus.OK);
    }

    // 커리큘럼 등록
    @PostMapping(value = "/manager/major/curriculum")
    public ResponseEntity<Boolean> registerCurriculum(@RequestBody MajorCurriculumRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerCurriculum(request), HttpStatus.OK);
    }

    // 커리큘럼 수정
    @PutMapping(value = "/manager/major/curriculum")
    public ResponseEntity<Boolean> updateCurriculum(@RequestParam(value = "id") Integer id, @RequestBody MajorCurriculumRequestDto request) {
        return new ResponseEntity<>(majorService.updateCurriculum(id, request), HttpStatus.OK);
    }

    // 커리큘럼 삭제
    @DeleteMapping(value = "/manager/major/curriculum")
    public ResponseEntity<Boolean> deleteCurriculum(@RequestParam(value = "id") Integer id) {
        return new ResponseEntity<>(majorService.deleteCurriculum(id), HttpStatus.OK);
    }

    // 커리큘럼 조회
    @GetMapping(value = "/major/curriculum")
    public ResponseEntity<Object> getCurriculum(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<Object>(majorService.getCurriculum(userDetails), headers, HttpStatus.OK);
    }

    // 연구실 등록
    @PostMapping(value = "/manager/major/lab")
    public ResponseEntity<Boolean> registerLab(@RequestBody MajorLabRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerLab(request), HttpStatus.OK);
    }

    // 연구실 수정
    @PutMapping(value = "/manager/major/lab")
    public ResponseEntity<Boolean> updateLab(@RequestParam(value = "id") Integer id, @RequestBody MajorLabRequestDto request) {
        return new ResponseEntity<>(majorService.updateLab(id, request), HttpStatus.OK);
    }

    // 연구실 삭제
    @DeleteMapping(value = "/manager/major/lab")
    public ResponseEntity<Boolean> deleteLab(@RequestParam(value = "id") Integer id) {
        return new ResponseEntity<>(majorService.deleteLab(id), HttpStatus.OK);
    }

    // 연구실 조회
    @GetMapping(value = "/major/lab")
    public ResponseEntity<Object> getLab(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<Object>(majorService.getLab(userDetails), headers, HttpStatus.OK);
    }

    // 학과 학생 조회
    @GetMapping(value = "/manager/major/student")
    public ResponseEntity<Object> getStudent(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<Object>(majorService.getStudent(userDetails), headers, HttpStatus.OK);
    }

    // 학과 정보 수정

    // 학과 정보 수정
}
