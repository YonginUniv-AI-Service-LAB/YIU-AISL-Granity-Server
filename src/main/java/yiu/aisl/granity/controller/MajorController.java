package yiu.aisl.granity.controller;

import com.google.api.Http;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.dto.*;
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

    // 학과 그룹 생성
    @PostMapping(value = "/manager/majorGroup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerMajorGroup(MajorGroupRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerMajorGroup(request), HttpStatus.OK);
    }

    // 교수님 등록
    @PostMapping(value = "/manager/major/professor", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerProfessor(MajorMemberRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerProfessor(request), HttpStatus.OK);
    }

    // 교수님 삭제
    @DeleteMapping(value = "/manager/major/professor")
    public ResponseEntity<Boolean> deleteProfessor(@RequestParam(value = "id") Integer majorMemberId) throws Exception {
        return new ResponseEntity<>(majorService.deleteProfessor(majorMemberId), HttpStatus.OK);
    }

    // 학생회 등록
    @PostMapping(value = "/manager/major/council", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerCouncil(MajorMemberRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerCouncil(request), HttpStatus.OK);
    }

    // 학생회 삭제
    @DeleteMapping(value = "/manager/major/council")
    public ResponseEntity<Boolean> deleteCouncil(@RequestParam(value = "id") Integer majorMemberId) throws Exception {
        return new ResponseEntity<>(majorService.deleteCouncil(majorMemberId), HttpStatus.OK);
    }

    // 커리큘럼 등록
    @PostMapping(value = "/manager/major/curriculum", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerCurriculum(MajorCurriculumRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerCurriculum(request), HttpStatus.OK);
    }

    // 커리큘럼 삭제
    @DeleteMapping(value = "/manager/major/curriculum")
    public ResponseEntity<Boolean> deleteCurriculum(@RequestParam(value = "id") Integer majorCurriculumId) throws Exception {
        return new ResponseEntity<>(majorService.deleteCurriculum(majorCurriculumId), HttpStatus.OK);
    }

    // 연구실 등록
    @PostMapping(value = "/manager/major/lab", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerLab(MajorLabRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerLab(request), HttpStatus.OK);
    }

    // 연구실 삭제
    @DeleteMapping(value = "/manager/major/lab")
    public ResponseEntity<Boolean> deleteLab(@RequestParam(value = "id") Integer majorCurriculumId) throws Exception {
        return new ResponseEntity<>(majorService.deleteLab(majorCurriculumId), HttpStatus.OK);
    }
}
