package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.controller.FileController;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Request.*;
import yiu.aisl.granity.dto.Response.FileResponseDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;
    private final MajorGroupRepository majorGroupRepository;
    private final MajorMemberRepository majorMemberRepository;
    private final MajorCurriculumRepository majorCurriculumRepository;
    private final MajorLabRepository majorLabRepository;
    private final MajorGroupCodeRepository majorGroupCodeRepository;
    private final UserRepository userRepository;
    private final UserMajorRepository userMajorRepository;
    private final NoticeRepository noticeRepository;
    private final FileController fileController;
    private final FileService fileService;

    // [API] 학과 정보 등록
    public boolean registerMajor(MajorRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getId() == null || request.getMajor().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // 데이터 중복 - 409
        if(majorRepository.existsById(request.getId())) {
            throw new CustomException(ErrorCode.DUPLICATE);
        }

        try {
            Major major = Major.builder()
                    .id(request.getId())
                    .major(request.getMajor())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            majorRepository.save(major);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 학과 정보 수정
    public boolean updateMajor(String majorId, MajorRequestDto request) throws Exception {
        // 해당 데이터 없음 - 404
        Major major = majorRepository.findById(majorId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        // 데이터 미입력 - 400
        if(request.getMajor().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            major.setMajor(request.getMajor());
            major.setUpdatedAt(LocalDateTime.now());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 학과 그룹 생성
    public MajorGroupCode registerMajorGroup(MajorGroupRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getMajorGroup().isEmpty() || request.getCode() == null || request.getMajor() == null ||
        request.getGreetings().isEmpty() || request.getAddress().isEmpty() || request.getTel().isEmpty() ||
        request.getEmail().isEmpty() || request.getFax().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        if(majorGroupRepository.existsByMajor(request.getMajor())) {
            throw new CustomException(ErrorCode.DUPLICATE);
        }

        // 해당 학과 존재 하지 않음 - 404
        if(!majorRepository.existsById(request.getMajor().getId())) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        MajorGroupCode mkMajorGroup = null;

        try {
            MajorGroup majorGroup = MajorGroup.builder()
                    .majorGroup(request.getMajorGroup())
                    .code(request.getCode())
                    .major(request.getMajor())
                    .greetings(request.getGreetings())
                    .address(request.getAddress())
                    .tel(request.getTel())
                    .email(request.getEmail())
                    .fax(request.getFax())
                    .color(request.getColor())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            majorGroupRepository.save(majorGroup);

            if(!majorGroupRepository.existsById(request.getCode())) {
                MajorGroupCode majorGroupCode = MajorGroupCode.builder()
                        .id(request.getCode())
                        .name(request.getMajorGroup())
                        .hidden(0)
                        .build();

                mkMajorGroup = majorGroupCodeRepository.save(majorGroupCode);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return mkMajorGroup;
    }

    // [API] 학과 그룹 hidden 처리 (hidden 컬럼 0 -> 1)
    public Boolean majorGroupHidden(String id, MajorGroupRequestDto request) throws Exception {
        // 히든 처리 하기 위한 majorGroupCode
        MajorGroupCode majorGroupCode = majorGroupCodeRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));
        majorGroupCode.setHidden(1);
        String code = majorGroupCode.getId();

        // 기존 코드를 이용한 major
        Major major = majorRepository.findById(code).orElseThrow();

        // 새롭게 추가된 majorGroupCode
        MajorGroupCode mkMajorGroupCode = registerMajorGroup(request);

        // 새롭게 추가된 majorGroup
        MajorGroup mkMajorGroup = majorGroupRepository.findSingleByCode(mkMajorGroupCode.getId());

        // 바꿔줘야할 majorGroup List
        List<MajorGroup> majorGroups = majorGroupRepository.findByCode(code);

        // 바꿔줘야할 user List
        List<User> assistants = userRepository.findByRoleAndMajor(2, major); // 조교
        List<User> professors = userRepository.findByRoleAndMajor(3, major); // 교수

        // 바꿔줘야할 notice List
        List<Notice> notices = noticeRepository.findByMajorGroupCode(majorGroupCode);

        // 바꿔줘야할 majorMember List
        List<MajorMember> majorMembers = majorMemberRepository.findByMajorGroupCode(majorGroupCode);

        // 바꿔줘야할 majorLab List
        List<MajorLab> majorLabs = majorLabRepository.findByMajorGroupCode(majorGroupCode);

        // 바꿔줘야할 majorCurriculum List
        List<MajorCurriculum> majorCurriculums = majorCurriculumRepository.findByMajorGroupCode(majorGroupCode);

        System.out.println("majorMembers size 출력 하기 : " +majorMembers.size());
        System.out.println("majorLabs size 출력 하기 : " +majorLabs.size());
        System.out.println("majorCurriculums size 출력 하기 : " +majorCurriculums.size());
        System.out.println("notices size 출력 하기 : " +notices.size());

        try {
            for(MajorGroup mg : majorGroups) {
                mg.setMajorGroup(mkMajorGroup.getMajorGroup());
                mg.setCode(mkMajorGroup.getCode());
                mg.setGreetings(mkMajorGroup.getGreetings());
                mg.setAddress(mkMajorGroup.getAddress());
                mg.setTel(mkMajorGroup.getTel());
                mg.setEmail(mkMajorGroup.getEmail());
                mg.setFax(mkMajorGroup.getFax());
                mg.setColor(mkMajorGroup.getColor());
                majorGroupRepository.save(mg);
            }
            // UserMajor 업데이트 (조교)
            for (User user : assistants) {
                List<UserMajor> userMajors = userMajorRepository.findByUser(user);
                for (UserMajor userMajor : userMajors) {
                    userMajor.setMajor(mkMajorGroup.getMajor());
                    userMajorRepository.save(userMajor);
                }
            }
            // UserMajor 업데이트 (교수)
            for (User user : professors) {
                List<UserMajor> userMajors = userMajorRepository.findByUser(user);
                for (UserMajor userMajor : userMajors) {
                    userMajor.setMajor(mkMajorGroup.getMajor());
                    userMajorRepository.save(userMajor);
                }
            }
            // Notice 업데이트
            for(Notice notice : notices) {
                System.out.println("notice 업데이트 진입");
                notice.setMajorGroupCode(mkMajorGroupCode);
                noticeRepository.save(notice);
            }
            // majorMember 업데이트
            for(MajorMember majorMember : majorMembers) {
                System.out.println("majorMember 업데이트 진입");
                majorMember.setMajorGroupCode(mkMajorGroupCode);
                majorMemberRepository.save(majorMember);
            }
            // majorLab 업데이트
            for(MajorLab majorLab : majorLabs) {
                majorLab.setMajorGroupCode(mkMajorGroupCode);
                majorLabRepository.save(majorLab);
            }
            // majorCurriculum 업데이트
            for(MajorCurriculum majorCurriculum : majorCurriculums) {
                majorCurriculum.setMajorGroupCode(mkMajorGroupCode);
                majorCurriculumRepository.save(majorCurriculum);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 교수님 등록
    public Boolean registerProfessor(MajorMemberRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getRole() == null || request.getName().isEmpty() || request.getContent1().isEmpty() || request.getTel().isEmpty() || request.getAddress().isEmpty() || request.getEmail().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<FileRequestDto> files = fileController.uploadFiles(request.getFiles());
        try {
            MajorMember professor = MajorMember.builder()
                    .majorGroupCode(groupCode)
                    .role(request.getRole())
                    .name(request.getName())
//                    .file(request.getFile())
                    .content1(request.getContent1())
                    .content2(request.getContent2())
                    .content3(request.getContent3())
                    .tel(request.getTel())
                    .address(request.getAddress())
                    .email(request.getEmail())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            MajorMember mkProfessor = majorMemberRepository.save(professor);
            fileService.saveFiles(8, mkProfessor.getId(), files);
        } catch (Exception e) {
            System.out.println("Error occurred while saving MajorMember: " +e);
            throw new Exception("Error occurred while saving MajorMember: " + e.getMessage(), e);
        }
        return true;
    }

    // [API] 교수님 삭제
    public Boolean deleteProfessor(Integer majorMemberId) throws Exception {
        // id 없음 - 404
        MajorMember professor = majorMemberRepository.findById(majorMemberId).orElseThrow(() ->
                 new CustomException(ErrorCode.NOT_EXIST_ID));
        List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorMemberId);

        majorMemberRepository.deleteById(professor.getId());
        fileController.deleteFiles(deleteFiles);
        fileService.deleteAllFileByTypeAndTypeId(8, majorMemberId); // DB 삭제
        return true;
    }

    // [API] 학생회 등록
    public Boolean registerCouncil(MajorMemberRequestDto request) throws Exception {
        // 데이터 없음 - 400
        if(request.getRole() == null || request.getName().isEmpty() || request.getContent1().isEmpty() || request.getTel().isEmpty() ||
        request.getAddress().isEmpty() || request.getEmail().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<FileRequestDto> files = fileController.uploadFiles(request.getFiles());
        try {
            MajorMember council = MajorMember.builder()
                    .majorGroupCode(groupCode)
                    .role(request.getRole())
                    .name(request.getName())
//                    .file(request.getFile())
                    .content1(request.getContent1())
                    .content2(request.getContent2())
                    .content3(request.getContent3())
                    .tel(request.getTel())
                    .address(request.getAddress())
                    .email(request.getEmail())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            MajorMember mkCouncil = majorMemberRepository.save(council);
            fileService.saveFiles(8, mkCouncil.getId(), files);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 학생회 삭제
    public Boolean deleteCouncil(Integer majorMemberId) throws Exception {
        // id 없음 - 404
        MajorMember council = majorMemberRepository.findById(majorMemberId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorMemberId);

        majorMemberRepository.deleteById(council.getId());
        fileController.deleteFiles(deleteFiles);
        fileService.deleteAllFileByTypeAndTypeId(8, majorMemberId); // DB 삭제
        return true;
    }

    // [API] 커리큘럼 등록
    public Boolean registerCurriculum(MajorCurriculumRequestDto request) throws Exception {
        // 데이터 없음 - 400
        if(request.getSubject().isEmpty() || request.getClassification() == null || request.getGrade() == null || request.getSemester() == null ||
        request.getCode() == null || request.getCredit() == null || request.getTheory() == null || request.getPractice() == null || request.getHidden() == null || request.getRequired() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        try {
            MajorCurriculum curriculum = MajorCurriculum.builder()
                    .majorGroupCode(groupCode)
                    .subject(request.getSubject())
                    .classification(request.getClassification())
                    .grade(request.getGrade())
                    .semester(request.getSemester())
                    .code(request.getCode())
                    .credit(request.getCredit())
                    .theory(request.getTheory())
                    .practice(request.getPractice())
                    .hidden(request.getHidden())
                    .required(request.getRequired())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            majorCurriculumRepository.save(curriculum);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 커리큘럼 삭제
    public Boolean deleteCurriculum(Integer majorCurriculumId) throws Exception {
        // id 없음 - 404
        MajorCurriculum curriculum = majorCurriculumRepository.findById(majorCurriculumId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        majorCurriculumRepository.deleteById(curriculum.getId());

        return true;
    }

    // [API] 연구실 등록
    public Boolean registerLab(MajorLabRequestDto request) throws Exception {
        // 데이터 없음 - 400
        if(request.getName().isEmpty() || request.getDescription().isEmpty() || request.getLink().isEmpty() ||
        request.getTel().isEmpty() || request.getEmail().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<FileRequestDto> files = fileController.uploadFiles(request.getFiles());

        try {
            MajorLab lab = MajorLab.builder()
                    .majorGroupCode(groupCode)
                    .name(request.getName())
                    .description(request.getDescription())
//                    .file(request.getFile())
                    .link(request.getLink())
                    .tel(request.getTel())
                    .email(request.getEmail())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            MajorLab mkLab = majorLabRepository.save(lab);
            fileService.saveFiles(8, mkLab.getId(), files);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }

    // [API] 연구실 삭제
    public Boolean deleteLab(Integer majorLabId) throws Exception {
        // id 없음 - 404
        MajorLab lab = majorLabRepository.findById(majorLabId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorLabId);

        fileController.deleteFiles(deleteFiles);
        fileService.deleteAllFileByTypeAndTypeId(8, majorLabId); // DB 삭제

        return true;
    }
}
