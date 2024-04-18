package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.*;
import yiu.aisl.granity.repository.*;
import yiu.aisl.granity.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;
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
    public Boolean registerProfessor(MajorMemberRegisterRequestDto request) {
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

    // [API] 교수님 삭제
    public Boolean deleteProfessor(Integer id) {
        majorMemberRepository.deleteById(String.valueOf(id));
        return true;
    }

    // [API] 교수님 수정
    public Boolean updateProfessor(Integer id, MajorMemberRegisterRequestDto request) {
        MajorMember professor = majorMemberRepository.findById(id);

        Major major = majorRepository.findById(request.getMajor()).orElseThrow();
        professor.setMajor(major);
        professor.setName(request.getName());
        professor.setImage(request.getImage());
        professor.setContent1(request.getContent1());
        professor.setContent2(request.getContent2());
        professor.setContent3(request.getContent3());
        professor.setTel(request.getTel());
        professor.setAddress(request.getAddress());
        professor.setEmail(request.getEmail());

        majorMemberRepository.save(professor);
        return true;
    }

    // [API] 학생회 등록
    public Boolean registerCouncil(MajorMemberRegisterRequestDto request) {
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

    // [API] 학생회 삭제
    public Boolean deleteCouncil(Integer id) {
        majorMemberRepository.deleteById(String.valueOf(id));
        return true;
    }

    // [API] 학생회 수정
    public Boolean updateCouncil(Integer id, MajorMemberRegisterRequestDto request) {
        MajorMember council = majorMemberRepository.findById(id);
        Major major = majorRepository.findById(request.getMajor()).orElseThrow();
        council.setMajor(major);
        council.setName(request.getName());
        council.setRole(request.getRole());
        council.setImage(request.getImage());
        council.setContent1(request.getContent1());
        council.setContent2(request.getContent2());
        council.setContent3(request.getContent3());
        council.setTel(request.getTel());
        council.setAddress(request.getAddress());
        council.setEmail(request.getEmail());

        majorMemberRepository.save(council);
        return true;
    }

    // [API] 커리큘럼 등록
    public Boolean registerCurriculum(MajorCurriculumRequestDto request) {
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

    // [API] 커리큘럼 삭제
    public Boolean deleteCurriculum(Integer id) {
        majorCurriculumRepository.deleteById(String.valueOf(id));
        return true;
    }

    // [API] 커리큘럼 수정
//    public Boolean updateCurriculum(Integer id, MajorCurriculumRequestDto request) {
//        MajorCurriculum curriculum = majorCurriculumRepository.findById(id);
//        Major major = majorRepository.findById(request.getMajor()).orElseThrow();
//        curriculum.setMajor(major);
//        curriculum.setName(request.getName());
//        curriculum.setClassification(request.getClassification());
//        curriculum.setGrade(request.getGrade());
//        curriculum.setSemester(request.getSemester());
//        curriculum.setCode(request.getCode());
//        curriculum.setCredit(request.getCredit());
//        curriculum.setTheory(request.getTheory());
//        curriculum.setPractice(request.getPractice());
//
//        majorCurriculumRepository.save(curriculum);
//        return true;
//    }

    // [API] 랩실 등록
    public Boolean registerLab(MajorLabRequestDto request) {
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

    // [API] 랩실 삭제
    public Boolean deleteLab(Integer id) {
        majorLabRepository.deleteById(String.valueOf(id));
        return true;
    }

    // [API] 랩실 수정
//    public Boolean updateLab(Integer id, MajorLabRequestDto request) {
//        MajorLab lab = majorLabRepository.findById(id);
//        Major major = majorRepository.findById(request.getMajor()).orElseThrow();
//        lab.setMajor(major);
//        lab.setName(request.getName());
//        lab.setDescription(request.getDescription());
//        lab.setImage(request.getImage());
//        lab.setLink(request.getLink());
//        lab.setTel(request.getTel());
//        lab.setEmail(request.getEmail());
//
//        majorLabRepository.save(lab);
//        return true;
//    }

    // [API] 학과 학생 조회
    public List<MajorStudentResponseDto> getStudent(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();

        List<MajorStudentResponseDto> student = userRepository.findAll().stream()
                .filter(user1 -> user1.getRole() == 1)
                .filter(user1 -> user1.getMajor_id1() == user.getMajor_id1() || user1.getMajor_id2() == user.getMajor_id1() || user1.getMajor_id3() == user.getMajor_id1())
                .map(MajorStudentResponseDto::new)
                .collect(Collectors.toList());

        return student;
    }
}
