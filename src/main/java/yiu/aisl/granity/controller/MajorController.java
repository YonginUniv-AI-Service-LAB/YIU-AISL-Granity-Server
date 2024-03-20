package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.granity.dto.MajorMemberRegisterRequestDto;
import yiu.aisl.granity.security.CustomUserDetails;
import yiu.aisl.granity.service.MajorService;

@RestController
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    // 교수님 등록
    @PostMapping(value = "/major/professor")
    public ResponseEntity<Boolean> registerProfessor(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MajorMemberRegisterRequestDto request) {
        return new ResponseEntity<Boolean>(majorService.registerProfessor(userDetails, request), HttpStatus.OK);
    }

    // 교수님 수정

    // 교수님 삭제

    // 교수님 조회

    // 학생회 등록

    // 학생회 수정

    // 학생회 삭제

    // 학생회 조회

    // 커리큘럼 등록

    // 커리큘럼 수정

    // 커리큘럼 삭제

    // 커리큘럼 조회

    // 연구실 등록

    // 연구실 수정

    // 연구실 삭제

    // 연구실 조회

    // 학과 학생 조회

    // 학과 정보 수정

    // 학과 정보 수정
}
