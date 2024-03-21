package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSOutput;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.*;
import yiu.aisl.granity.repository.*;
import yiu.aisl.granity.security.CustomUserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MajorService {
    private final UserRepository userRepository;
    private final MajorMemberRepository majorMemberRepository;
    private final MajorRepository majorRepository;
    private final MajorCurriculumRepository majorCurriculumRepository;
    private final MajorLabRepository majorLabRepository;

    // [API] 교수님 등록
    public Boolean registerProfessor(CustomUserDetails userDetails, MajorMemberRegisterRequestDto request) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        if(user.getRole() != 3) {
            new Exception("작업 권한 없음");
        }
        Major major = majorRepository.findById(request.getMajor()).orElseThrow();

        try {
            MajorMember professor = MajorMember.builder()
                    .major(major)
                    .role(request.getRole())
                    .name(request.getName())
                    .image(request.getImage())
                    .content1(request.getContent1()) // 커리어, 연구과제, 경력사항 등
                    .content2(request.getContent2()) // 커리어, 연구과제, 경력사항 등
                    .content3(request.getContent3()) // 커리어, 연구과제, 경력사항 등
                    .tel(request.getTel())
                    .address(request.getAddress())
                    .email(request.getEmail())
                    .build();

            majorMemberRepository.save(professor);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }

    // [API] 교수님 조회
    public List<MajorMemberRegisterResponseDto> getProfessor(CustomUserDetails userDetails) {
        List<MajorMemberRegisterResponseDto> professor = majorMemberRepository.findAll().stream()
                .filter(majorMember -> majorMember.getRole() == 2)
                .map(MajorMemberRegisterResponseDto::new)
                .collect(Collectors.toList());

        return professor;
    }

    // [API] 학생회 등록
    public Boolean registerCouncil(CustomUserDetails userDetails, MajorMemberRegisterRequestDto request) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        if(user.getRole() != 3) {
            new Exception("작업 권한 없음");
        }
        Major major = majorRepository.findById(request.getMajor()).orElseThrow();

        try {
            MajorMember council = MajorMember.builder()
                    .major(major)
                    .role(request.getRole())
                    .name(request.getName())
                    .image(request.getImage())
                    .content1(request.getContent1()) // 커리어, 연구과제, 경력사항 등
                    .content2(request.getContent2()) // 커리어, 연구과제, 경력사항 등
                    .content3(request.getContent3()) // 커리어, 연구과제, 경력사항 등
                    .tel(request.getTel())
                    .address(request.getAddress())
                    .email(request.getEmail())
                    .build();

            majorMemberRepository.save(council);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }

    // [API] 학생회 조회
    public List<MajorMemberRegisterResponseDto> getCouncil(CustomUserDetails userDetails) {
        List<MajorMemberRegisterResponseDto> professor = majorMemberRepository.findAll().stream()
                .filter(majorMember -> majorMember.getRole() == 1)
                .map(MajorMemberRegisterResponseDto::new)
                .collect(Collectors.toList());

        return professor;
    }

    // [API] 커리큘럼 등록
    public Boolean registerCurriculum(CustomUserDetails userDetails, MajorCurriculumRequestDto request) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        if(user.getRole() != 3) {
            new Exception("작업 권한 없음");
        }
        Major major = majorRepository.findById(request.getMajor()).orElseThrow();

        try {
            MajorCurriculum council = MajorCurriculum.builder()
                    .major(major)
                    .classification(request.getClassification()) // 기초전공 인지 여부
                    .name(request.getName()) // 과목명
                    .grade(request.getGrade()) // 학년
                    .semester(request.getSemester()) // 학기
                    .code(request.getCode()) // 학수번호
                    .credit(request.getCredit()) // 학점
                    .theory(request.getTheory()) // 이론 몇 실기 몇 구부
                    .practice(request.getPractice()) // 전공 or 실기 여부
                    .build();

            majorCurriculumRepository.save(council);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }

    // [API] 커리큘럼 조회
    public List<MajorCurriculumResponseDto> getCurriculum(CustomUserDetails userDetails) {
        List<MajorCurriculumResponseDto> curriculum = majorCurriculumRepository.findAll().stream()
                .map(MajorCurriculumResponseDto::new)
                .collect(Collectors.toList());

        return curriculum;
    }

    // [API] 랩실 등록
    public Boolean registerLab(CustomUserDetails userDetails, MajorLabRequestDto request) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        if(user.getRole() != 3) {
            new Exception("작업 권한 없음");
        }
        Major major = majorRepository.findById(request.getMajor()).orElseThrow();

        try {
            MajorLab lab = MajorLab.builder()
                    .major(major)
                    .name(request.getName()) // 과목명
                    .description(request.getDescription())
                    .image(request.getImage())
                    .link(request.getLink())
                    .tel(request.getTel())
                    .email(request.getEmail())
                    .build();

            majorLabRepository.save(lab);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }

    // [API] 랩실 조회
    public List<MajorLabResponseDto> getLab(CustomUserDetails userDetails) {
        List<MajorLabResponseDto> lab = majorLabRepository.findAll().stream()
                .map(MajorLabResponseDto::new)
                .collect(Collectors.toList());

        return lab;
    }
}
