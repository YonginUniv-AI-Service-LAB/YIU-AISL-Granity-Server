package yiu.aisl.granity.controller;

import com.google.api.Http;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorGroup;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.dto.Request.*;
import yiu.aisl.granity.dto.Response.*;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.service.MajorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    // 전체 학과 조회
    @GetMapping(value = "/major")
    public List<MajorResponseDto> getMajors() throws Exception {
        return new ResponseEntity<>(majorService.getMajors(), HttpStatus.OK).getBody();
    }

    @GetMapping(value = "/major/detail")
    public MajorGroupResponseDto getMajorDetail(@RequestParam(value = "id") Major id) throws Exception {
        return new ResponseEntity<>(majorService.getMajorDetail(id), HttpStatus.OK).getBody();
    }

    // 학과 학생 조회
    @GetMapping(value = "/manager/major/student")
    public List<UserResponseDto> getStudents(@RequestParam(value = "id") String id) throws Exception {
        return new ResponseEntity<>(majorService.getStudents(id), HttpStatus.OK).getBody();
    }

    // 학과 정보 등록
    @PostMapping(value = "/manager/major", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerMajor(MajorRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerMajor(request), HttpStatus.OK);
    }

    // 학과 그룹 생성
    @PostMapping(value = "/manager/majorGroup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<MajorGroupCode> registerMajorGroup(MajorGroupRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerMajorGroup(request), HttpStatus.OK);
    }

    // 학과 그룹 수정
    @PutMapping(value = "/manager/majorGroupInfo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> updateMajorGroup(@RequestParam(value = "id") Major major, MajorGroupRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.updateMajorGroup(major, request), HttpStatus.OK);
    }

    // 학과 그룹 코드 조회
    @GetMapping(value = "/majorGroupCode")
    public List<MajorGroupCodeResponseDto> getMajorGroupCode() throws Exception {
        return new ResponseEntity<>(majorService.getMajorGroupCode(), HttpStatus.OK).getBody();
    }

    // 학과 그룹 hidden 처리 (hidden 컬럼 0 -> 1)
    @PutMapping(value = "/manager/majorGroup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> majorGroupHidden(MajorGroupRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.majorGroupHidden(request), HttpStatus.OK);
    }

    // 연혁 조회
    @GetMapping(value = "/major/history")
    public ResponseEntity<List<YearlyEvents>> getHistories(@RequestParam(value = "id") Major major) throws Exception {
        return new ResponseEntity<>(majorService.getHistories(major), HttpStatus.OK);
    }

    // 연혁 등록
    @PostMapping(value = "/manager/major/history", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> registerHistory(MajorHistoryRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerHistory(request), HttpStatus.OK);
    }

    // 연혁 삭제
    @DeleteMapping(value = "/manager/major/history")
    public ResponseEntity<Boolean> deleteHistory(@RequestParam(value = "id") Integer id) throws Exception {
        return new ResponseEntity<>(majorService.deleteHistory(id), HttpStatus.OK);
    }

    // 연혁 수정
    @PutMapping(value = "/manager/major/history", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> updateHistory(@RequestParam(value = "id") Integer id, MajorHistoryRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.updateHistory(id, request), HttpStatus.OK);
    }

    // 교수님 조회
    @GetMapping(value = "/major/professor")
    public ResponseEntity<List<MajorMemberResponseDto>> getProfessors(@RequestParam(value = "id") MajorGroupCode majorGroupCode) throws Exception {
        return new ResponseEntity<>(majorService.getProfessors(majorGroupCode), HttpStatus.OK);
    }

    // 교수님 등록
    @PostMapping(value = "/manager/major/professor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> registerProfessor(MajorMemberRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerProfessor(request), HttpStatus.OK);
    }

    // 교수님 삭제
    @DeleteMapping(value = "/manager/major/professor")
    public ResponseEntity<Boolean> deleteProfessor(@RequestParam(value = "id") Integer majorMemberId) throws Exception {
        return new ResponseEntity<>(majorService.deleteProfessor(majorMemberId), HttpStatus.OK);
    }

    // 교수님 수정
    @PutMapping(value = "/manager/major/professor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateProfessor(@RequestParam(value = "id") Integer majorMemberId, MajorMemberRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.updateProfessor(majorMemberId, request), HttpStatus.OK);
    }

    // 학생회 조회
    @GetMapping(value = "/major/council")
    public ResponseEntity<List<MajorMemberResponseDto>> getCouncils(@RequestParam(value = "id") MajorGroupCode majorGroupCode) throws Exception {
        return new ResponseEntity<>(majorService.getCouncils(majorGroupCode), HttpStatus.OK);
    }

    // 학생회 등록
    @PostMapping(value = "/manager/major/council", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> registerCouncil(MajorMemberRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerCouncil(request), HttpStatus.OK);
    }

    // 학생회 삭제
    @DeleteMapping(value = "/manager/major/council")
    public ResponseEntity<Boolean> deleteCouncil(@RequestParam(value = "id") Integer majorMemberId) throws Exception {
        return new ResponseEntity<>(majorService.deleteCouncil(majorMemberId), HttpStatus.OK);
    }

    // 학생회 수정
    @PutMapping(value = "/manager/major/council", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateCouncil(@RequestParam(value = "id") Integer majorMemberId, MajorMemberRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.updateCouncil(majorMemberId, request), HttpStatus.OK);
    }

    // 커리큘럼 조회
    @GetMapping(value = "/major/curriculum")
    public ResponseEntity<List<MajorCurriculumResponseDto>> getCurriculums(@RequestParam(value = "id") MajorGroupCode majorGroupCode) throws Exception {
        return new ResponseEntity<>(majorService.getCurriculums(majorGroupCode), HttpStatus.OK);
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

    // 커리큘럼 수정
    @PutMapping(value = "/manager/major/curriculum", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> updateCurriculum(@RequestParam(value = "id") Integer majorCurriculumId, MajorCurriculumRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.updateCurriculum(majorCurriculumId, request), HttpStatus.OK);
    }

    // 연구실 조회
    @GetMapping(value = "/major/lab")
    public ResponseEntity<List<MajorLabResponseDto>> getLabs(@RequestParam(value = "id") MajorGroupCode majorGroupCode) throws Exception {
        return new ResponseEntity<>(majorService.getLabs(majorGroupCode), HttpStatus.OK);
    }

    // 연구실 등록
    @PostMapping(value = "/manager/major/lab", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> registerLab(MajorLabRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.registerLab(request), HttpStatus.OK);
    }

    // 연구실 삭제
    @DeleteMapping(value = "/manager/major/lab")
    public ResponseEntity<Boolean> deleteLab(@RequestParam(value = "id") Integer majorCurriculumId) throws Exception {
        return new ResponseEntity<>(majorService.deleteLab(majorCurriculumId), HttpStatus.OK);
    }

    // 연구실 수정
    @PutMapping(value = "/manager/major/lab", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateLab(@RequestParam(value = "id") Integer majorCurriculumId, MajorLabRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.updateLab(majorCurriculumId, request), HttpStatus.OK);
    }

    // 유저 전공 추가
    @PostMapping(value = "/manager/user/major", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> addUserMajor(UserMajorRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.addUserMajor(request), HttpStatus.OK);
    }

    @PostMapping(value = "/manager/requiredsubject", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> addRequiredSubject(@RequestParam(value = "id") String major, RequiredSubjectRequestDto request) throws Exception {
        return new ResponseEntity<>(majorService.addRequiredSubject(major, request), HttpStatus.OK);
    }
}
