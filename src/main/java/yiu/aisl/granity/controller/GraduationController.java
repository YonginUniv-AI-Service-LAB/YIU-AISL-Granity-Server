package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.dto.Request.MajorGraduationRequestDto;
import yiu.aisl.granity.dto.Request.UserGraduationRequestDto;
import yiu.aisl.granity.dto.Response.MajorGraduationResponseDto;
import yiu.aisl.granity.dto.Response.UserGraduationGroupResponseDto;
import yiu.aisl.granity.dto.Response.UserGraduationResponseDto;
import yiu.aisl.granity.dto.Response.UserResponseDto;
import yiu.aisl.granity.service.GraduationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GraduationController {
    private final GraduationService graduationService;

    // 졸업요건 처리현황 조회
    @GetMapping(value = "/manager/graduation")
    public ResponseEntity<List<UserGraduationGroupResponseDto>> getUserGraduationStatus(@RequestParam(value = "id") String id) throws Exception {
        return new ResponseEntity<>(graduationService.getUserGraduationStatus(id), HttpStatus.OK);
    }

    // 졸업요건 조회
    @GetMapping(value = "/manager/graduation/requirement")
    public ResponseEntity<List<MajorGraduationResponseDto>> getMajorGraduation(@RequestParam(value = "id") Major id) throws Exception {
        return new ResponseEntity<>(graduationService.getMajorGraduations(id), HttpStatus.OK);
    }

    // 졸업요건 등록
    @PostMapping(value = "/manager/graduation/requirement", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerGraduationRequirement(MajorGraduationRequestDto request) throws Exception {
        return new ResponseEntity<>(graduationService.registerGraduationRequirement(request), HttpStatus.OK);
    }

    // 졸업요건 삭제
    @DeleteMapping(value = "/manager/graduation/requirement")
    public ResponseEntity<Boolean> deleteMajorGraduation(@RequestParam(value = "id") Integer id) throws Exception {
        return new ResponseEntity<>(graduationService.deleteMajorGraduation(id), HttpStatus.OK);
    }

    // 졸업요건 수정
    @PutMapping(value = "/manager/graduation/requirement")
    public ResponseEntity<Boolean> updateMajorGraduation(@RequestParam(value = "id") Integer id, MajorGraduationRequestDto request) throws Exception {
        return new ResponseEntity<>(graduationService.updateMajorGraduation(id, request), HttpStatus.OK);
    }

    // 학생 졸업 요건 등록
    @PostMapping(value = "/manager/student/graduation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerStudentGraduation(UserGraduationRequestDto request) throws Exception {
        return new ResponseEntity<>(graduationService.registerStudentGraduation(request), HttpStatus.OK);
    }

    // 학생 졸업 요건 삭제 (특정 학생의 특정 졸업 요건 삭제)
    @DeleteMapping(value = "/manager/student/graduation")
    public ResponseEntity<Boolean> deleteStudentGraduation(@RequestParam(value = "id") Integer id) throws Exception {
        return new ResponseEntity<>(graduationService.deleteStudentGraduation(id), HttpStatus.OK);
    }

    // 학생 졸업 요건 삭제 (선택된 졸업 요건 삭제)
    @DeleteMapping(value = "/manager/student/graduations", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> deleteStudentGraduationSelected(UserGraduationRequestDto request) throws Exception {
        return new ResponseEntity<>(graduationService.deleteStudentGraduationSelected(request), HttpStatus.OK);
    }

    // 졸업 요건 제출
    @PostMapping(value = "/graduation/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> applyGraduation(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "id") Integer id, @RequestParam(value = "major") Major major, UserGraduationRequestDto request) throws Exception {
        return new ResponseEntity<>(graduationService.applyGraduation(userDetails, id, major, request), HttpStatus.OK);
    }

    // 졸업 요건 승인
    @PutMapping(value = "/manager/graduation/approval")
    public ResponseEntity<Boolean> approvalGraduation(@RequestParam(value = "id") Integer id) throws Exception {
        return new ResponseEntity<>(graduationService.approvalGraduation(id), HttpStatus.OK);
    }

    // 졸업 요건 거절
    @PutMapping(value = "/manager/graduation/rejection", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> rejectionGraduation(@RequestParam(value = "id") Integer id, UserGraduationRequestDto request) throws Exception {
        return new ResponseEntity<>(graduationService.rejectionGraduation(id, request), HttpStatus.OK);
    }

    // 졸업 예정자 조회
    @GetMapping(value = "/manager/graduation/student")
    public ResponseEntity<List<UserResponseDto>> getGraduationStudents(@RequestParam("id") String id) throws Exception {
        return new ResponseEntity<>(graduationService.getGraduationStudents(id), HttpStatus.OK);
    }
}
