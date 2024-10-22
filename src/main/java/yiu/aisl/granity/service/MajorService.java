package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.controller.FileController;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Request.*;
import yiu.aisl.granity.dto.Response.*;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.reverse;

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
    private final MajorHistoryRepository majorHistoryRepository;
    private final UserRepository userRepository;
    private final UserMajorRepository userMajorRepository;
    private final NoticeRepository noticeRepository;
    private final FaqRepository faqRepository;
    private final BoardRepository boardRepository;
    private final FileController fileController;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final RequiredSubjectRepository requiredSubjectRepository;

    // [API] 전체 학과 조회
    public List<MajorResponseDto> getMajors() throws Exception {
        List<Major> majors = majorRepository.findAll();
        List<MajorResponseDto> getListDto = new ArrayList<>();
        for(Major code : majors) {
            getListDto.add(MajorResponseDto.GetMajorDto(code));
        }
        return getListDto;
    }

    // [API] 학과 상세 조회
    public MajorGroupResponseDto getMajorDetail(Major id) throws Exception {
        MajorGroup majorGroup = majorGroupRepository.findByMajor(id);

        List<MajorGroup> majorGroups = majorGroupRepository.findByCode(majorGroup.getCode());
        System.out.println(majorGroups);
        List<String> majorIds = majorGroups.stream()
                .map(mg -> mg.getMajor().getId())  // 각 Major의 ID 추출
                .distinct()  // 중복된 Major ID가 있을 경우 제거
                .collect(Collectors.toList());


        System.out.println(majorIds);

        List<Major> majors = majorRepository.findByIdIn(majorIds);
        return MajorGroupResponseDto.GetMajorGroupDto(majorGroup, majors);
    }

    // [API] 학과 학생 조회
    public List<UserResponseDto> getStudents(String majorGroupCode) throws Exception {
        List<User> students = userRepository.findByRoleAndMajorGroupCode(1, majorGroupCode);
        List<UserResponseDto> getListDto = new ArrayList<>();
        for(User user : students) {
            getListDto.add(UserResponseDto.GetUserDto(user));
        }
        return getListDto;
    }

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

    // [API] 학과 그룹 수정
    public Boolean updateMajorGroup(Major major, MajorGroupRequestDto request) throws Exception {
        MajorGroup majorGroup;
        if(majorGroupRepository.existsByMajor(major)) {
            majorGroup = majorGroupRepository.findByMajor(major);
        } else throw new CustomException(ErrorCode.NOT_EXIST_ID);

        // 데이터 미입력 - 400
        if(request.getGreetings().isEmpty() || request.getAddress().isEmpty() || request.getTel().isEmpty() ||
                request.getEmail().isEmpty() || request.getFax().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            majorGroup.setGreetings(request.getGreetings());
            majorGroup.setAddress(request.getAddress());
            majorGroup.setTel(request.getTel());
            majorGroup.setEmail(request.getEmail());
            majorGroup.setFax(request.getFax());

            majorGroupRepository.save(majorGroup);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 학과 그룹 코드 조회
    public List<MajorGroupCodeResponseDto> getMajorGroupCode() throws Exception {
        List<MajorGroupCode> codes = majorGroupCodeRepository.findAll();
        List<MajorGroupCodeResponseDto> getListDto = new ArrayList<>();
        for(MajorGroupCode code : codes) {
            getListDto.add(MajorGroupCodeResponseDto.GetMajorGroupCodeDto(code));
        }
        return getListDto;
    }

    // [API] 학과 그룹 hidden 처리 (hidden 컬럼 0 -> 1)
    public Boolean majorGroupHidden(MajorGroupRequestDto request) throws Exception {
        // 히든 처리 하기 위한 majorGroupCode 리스트
        List<MajorGroupCode> majorGroupCodes = majorGroupCodeRepository.findAllById(request.getCodes());

        try {
            // 새롭게 추가된 majorGroupCode
            MajorGroupCode mkMajorGroupCode = registerMajorGroup(request);

            for (MajorGroupCode majorGroupCode : majorGroupCodes) {
                majorGroupCode.setHidden(1);

                String code = majorGroupCode.getId();
                System.out.println("code 값 출력 확인 : " +code);

                // 기존 코드를 이용한 major
                Major major = majorRepository.findById(code).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_ID));

                // 새롭게 추가된 majorGroup
                List<MajorGroup> mkMajorGroup = majorGroupRepository.findByCode(mkMajorGroupCode.getId());

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

                // 바꿔줘야할 board List
                List<Board> boards = boardRepository.findByMajorGroupCode(majorGroupCode);

                // 바꿔줘야할 FAQ List
                List<Faq> faqs = faqRepository.findByMajorGroupCode(majorGroupCode);

                // Update 관련 엔티티들
                for (MajorGroup mg : majorGroups) {
                    mg.setCode(mkMajorGroup.get(0).getCode());
                    majorGroupRepository.save(mg);
                }

                // UserMajor 업데이트 (조교)
                for (User user : assistants) {
                    List<UserMajor> userMajors = userMajorRepository.findByUser(user);
                    for (UserMajor userMajor : userMajors) {
                        userMajor.setMajor(mkMajorGroup.get(0).getMajor());
                        userMajorRepository.save(userMajor);
                    }
                }

                // UserMajor 업데이트 (교수)
                for (User user : professors) {
                    List<UserMajor> userMajors = userMajorRepository.findByUser(user);
                    for (UserMajor userMajor : userMajors) {
                        userMajor.setMajor(mkMajorGroup.get(0).getMajor());
                        userMajorRepository.save(userMajor);
                    }
                }

                // Notice 업데이트
                for (Notice notice : notices) {
                    notice.setMajorGroupCode(mkMajorGroupCode);
                    noticeRepository.save(notice);
                }

                // majorMember 업데이트
                for (MajorMember majorMember : majorMembers) {
                    majorMember.setMajorGroupCode(mkMajorGroupCode);
                    majorMemberRepository.save(majorMember);
                }

                // majorLab 업데이트
                for (MajorLab majorLab : majorLabs) {
                    majorLab.setMajorGroupCode(mkMajorGroupCode);
                    majorLabRepository.save(majorLab);
                }

                // majorCurriculum 업데이트
                for (MajorCurriculum majorCurriculum : majorCurriculums) {
                    majorCurriculum.setMajorGroupCode(mkMajorGroupCode);
                    majorCurriculumRepository.save(majorCurriculum);
                }

                // board 업데이트
                for (Board board : boards) {
                    board.setMajorGroupCode(mkMajorGroupCode);
                    boardRepository.save(board);
                }

                // FAQ 업데이트
                for (Faq faq : faqs) {
                    faq.setMajorGroupCode(mkMajorGroupCode);
                    faqRepository.save(faq);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        System.out.println("성공");
        return true;
    }


    // [API] 연혁 조회
    public List<YearlyEvents> getHistories(Major major) throws Exception {
        List<MajorHistory> histories = majorHistoryRepository.findAllByMajor(major);
        return reverse(MajorHistoryResponseDto.groupByYear(histories));
    }

    // [API] 연혁 등록
    public Boolean registerHistory(MajorHistoryRequestDto request) throws Exception {
        if(request.getMajor() == null || request.getYear() == null || request.getMonth() == null || request.getEvent().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        Major major = majorRepository.findById(request.getMajor().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        try {
            MajorHistory majorHistory = MajorHistory.builder()
                    .major(major)
                    .year(request.getYear())
                    .month(request.getMonth())
                    .event(request.getEvent())
                    .build();

            majorHistoryRepository.save(majorHistory);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }

    // [API] 연혁 삭제
    public Boolean deleteHistory(Integer id) throws Exception {
        MajorHistory majorHistory = majorHistoryRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        majorHistoryRepository.deleteById(id);
        return true;
    }

    // [API] 연혁 수정
    public Boolean updateHistory(Integer id, MajorHistoryRequestDto request) throws Exception {
        MajorHistory majorHistory = majorHistoryRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        if(request.getMajor() == null || request.getYear() == null || request.getMonth() == null || request.getEvent().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        Major major = majorRepository.findById(request.getMajor().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        try {
            majorHistory.setYear(request.getYear());
            majorHistory.setMonth(request.getMonth());
            majorHistory.setEvent(request.getEvent());
            majorHistory.setMajor(major);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 교수님 조회
    public List<MajorMemberResponseDto> getProfessors(MajorGroupCode majorGroupCode) throws Exception {
        List<MajorMember> professors = majorMemberRepository.findAllByMajorGroupCodeAndRole(majorGroupCode, 2);
        List<MajorMemberResponseDto> getListDto = new ArrayList<>();
        for(MajorMember professor : professors) {
            List<File> files = fileRepository.findAllByTypeAndTypeId(8, professor.getId());
            getListDto.add(MajorMemberResponseDto.GetMajorMemberDto(professor, files));
        }
        return getListDto;
    }

    // [API] 교수님 등록
    public Boolean registerProfessor(MajorMemberRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getRole() == null || request.getName().isEmpty() || request.getContent1().isEmpty() || request.getTel().isEmpty() || request.getAddress().isEmpty() || request.getEmail().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));
        if(groupCode.getHidden() == 1) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

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

        if(professor.getRole() == 2) {
            List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorMemberId);

            majorMemberRepository.deleteById(professor.getId());
            fileController.deleteFiles(deleteFiles);
            fileService.deleteAllFileByTypeAndTypeId(8, majorMemberId); // DB 삭제

            return true;
        } else throw new CustomException(ErrorCode.USER_DATA_INCONSISTENCY);
    }

    // [API] 교수님 수정
    public Boolean updateProfessor(Integer majorMemberId, MajorMemberRequestDto request) {
        // id 없음 - 404
        MajorMember professor = majorMemberRepository.findById(majorMemberId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        // 데이터 미입력 - 400
        if(request.getRole() == null || request.getName().isEmpty() || request.getContent1().isEmpty() || request.getTel().isEmpty() || request.getAddress().isEmpty() || request.getEmail().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        if(professor.getRole() != 2) {
            throw new CustomException(ErrorCode.USER_DATA_INCONSISTENCY);
        }

        try {
            professor.setRole(request.getRole());
            professor.setName(request.getName());
            professor.setContent1(request.getContent1());
            professor.setContent2(request.getContent2());
            professor.setContent3(request.getContent3());
            professor.setTel(request.getTel());
            professor.setAddress(request.getAddress());
            professor.setEmail(request.getEmail());

            // 삭제할 파일 정보 조회
            List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorMemberId);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);
            fileService.deleteAllFileByTypeAndTypeId(8, majorMemberId); // DB 삭제

            // 파일 업로드
            List<FileRequestDto> uploadFiles = fileController.uploadFiles(request.getFiles());

            // 파일 정보 저장
            fileService.saveFiles(8, majorMemberId, uploadFiles);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 학생회 조회
    public List<MajorMemberResponseDto> getCouncils(MajorGroupCode majorGroupCode) throws Exception {
        List<MajorMember> councils = majorMemberRepository.findAllByMajorGroupCodeAndRole(majorGroupCode, 1);
        List<MajorMemberResponseDto> getListDto = new ArrayList<>();
        for(MajorMember council : councils) {
            List<File> files = fileRepository.findAllByTypeAndTypeId(8, council.getId());
            getListDto.add(MajorMemberResponseDto.GetMajorMemberDto(council, files));
        }
        return getListDto;
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

        if(groupCode.getHidden() == 1) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

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

        if(council.getRole() == 2) {
            throw new CustomException(ErrorCode.USER_DATA_INCONSISTENCY);
        }

        List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorMemberId);

        majorMemberRepository.deleteById(council.getId());
        fileController.deleteFiles(deleteFiles);
        fileService.deleteAllFileByTypeAndTypeId(8, majorMemberId); // DB 삭제
        return true;
    }

    // [API] 학생회 수정
    public Boolean updateCouncil(Integer majorMemberId, MajorMemberRequestDto request) {
        // id 없음 - 404
        MajorMember council = majorMemberRepository.findById(majorMemberId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        // 데이터 미입력 - 400
        if(request.getRole() == null || request.getName().isEmpty() || request.getContent1().isEmpty() || request.getTel().isEmpty() || request.getAddress().isEmpty() || request.getEmail().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        if(council.getRole() == 2) {
            throw new CustomException(ErrorCode.USER_DATA_INCONSISTENCY);
        }

        try {
            council.setRole(request.getRole());
            council.setName(request.getName());
            council.setContent1(request.getContent1());
            council.setContent2(request.getContent2());
            council.setContent3(request.getContent3());
            council.setTel(request.getTel());
            council.setAddress(request.getAddress());
            council.setEmail(request.getEmail());

            // 삭제할 파일 정보 조회
            List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorMemberId);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);
            fileService.deleteAllFileByTypeAndTypeId(8, majorMemberId); // DB 삭제

            // 파일 업로드
            List<FileRequestDto> uploadFiles = fileController.uploadFiles(request.getFiles());

            // 파일 정보 저장
            fileService.saveFiles(8, majorMemberId, uploadFiles);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 커리큘럼 조회
    public List<MajorCurriculumResponseDto> getCurriculums(MajorGroupCode majorGroupCode) throws Exception {
        List<MajorCurriculum> curriculums = majorCurriculumRepository.findAllByMajorGroupCode(majorGroupCode);
        List<MajorCurriculumResponseDto> getListDto = new ArrayList<>();
        for(MajorCurriculum curriculum : curriculums) {
            getListDto.add(MajorCurriculumResponseDto.GetMajorCurriculumDto(curriculum));
        }
        return getListDto;
    }

    // [API] 커리큘럼 등록
    public Boolean registerCurriculum(MajorCurriculumRequestDto request) throws Exception {
        // 데이터 없음 - 400
        if(request.getContents().isEmpty() || request.getSubject().isEmpty() || request.getClassification() == null || request.getGrade() == null || request.getSemester() == null ||
        request.getCode() == null || request.getCredit() == null || request.getTheory() == null || request.getPractice() == null || request.getHidden() == null || request.getRequired() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        if(groupCode.getHidden() == 1) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

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
                    .contents(request.getContents())
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

    // [API] 커리큘럼 수정
    public Boolean updateCurriculum(Integer majorCurriculumId, MajorCurriculumRequestDto request) throws Exception {
        // id 없음 - 404
        MajorCurriculum curriculum = majorCurriculumRepository.findById(majorCurriculumId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        // 데이터 없음 - 400
        if(request.getSubject().isEmpty() || request.getClassification() == null || request.getGrade() == null || request.getSemester() == null ||
                request.getCode() == null || request.getCredit() == null || request.getTheory() == null || request.getPractice() == null || request.getHidden() == null || request.getRequired() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            curriculum.setSubject(request.getSubject());
            curriculum.setClassification(request.getClassification());
            curriculum.setGrade(request.getGrade());
            curriculum.setSemester(request.getSemester());
            curriculum.setCode(request.getCode());
            curriculum.setCredit(request.getCredit());
            curriculum.setTheory(request.getTheory());
            curriculum.setPractice(request.getPractice());
            curriculum.setHidden(request.getHidden());
            curriculum.setRequired(request.getRequired());
            curriculum.setContents(request.getContents());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 연구실 조회
    public List<MajorLabResponseDto> getLabs(MajorGroupCode majorGroupCode) throws Exception {
        List<MajorLab> labs = majorLabRepository.findAllByMajorGroupCode(majorGroupCode);
        List<MajorLabResponseDto> getListDto = new ArrayList<>();
        for(MajorLab lab : labs) {
            List<File> files = fileRepository.findAllByTypeAndTypeId(8, lab.getId());
            getListDto.add(MajorLabResponseDto.GetMajorLabDto(lab, files));
        }
        return getListDto;
    }

    // [API] 연구실 등록
    public Boolean registerLab(MajorLabRequestDto request) throws Exception {
        // 데이터 없음 - 400
        if(request.getName().isEmpty() || request.getDescription().isEmpty() || request.getLink().isEmpty() ||
        request.getTel().isEmpty() || request.getEmail().isEmpty() || request.getProfessor().isEmpty() || request.getAddress().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        if(groupCode.getHidden() == 1) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

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
                    .professor(request.getProfessor())
                    .address(request.getAddress())
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

        majorLabRepository.deleteById(majorLabId);

        return true;
    }

    // [API] 연구실 수정
    public Boolean updateLab(Integer majorLabId, MajorLabRequestDto request) throws Exception {
        // id 없음 - 404
        MajorLab lab = majorLabRepository.findById(majorLabId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        // 데이터 없음 - 400
        if(request.getName().isEmpty() || request.getDescription().isEmpty() || request.getLink().isEmpty() ||
                request.getTel().isEmpty() || request.getEmail().isEmpty() || request.getProfessor().isEmpty() || request.getAddress().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            lab.setName(request.getName());
            lab.setDescription(request.getDescription());
            lab.setLink(request.getLink());
            lab.setTel(request.getTel());
            lab.setEmail(request.getEmail());
            lab.setProfessor(request.getProfessor());
            lab.setAddress(request.getAddress());

            // 삭제할 파일 정보 조회
            List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(8, majorLabId);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);
            fileService.deleteAllFileByTypeAndTypeId(8, majorLabId); // DB 삭제

            // 파일 업로드
            List<FileRequestDto> uploadFiles = fileController.uploadFiles(request.getFiles());

            // 파일 정보 저장
            fileService.saveFiles(8, majorLabId, uploadFiles);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 전공 추가
    public Boolean addUserMajor(UserMajorRequestDto request) throws Exception {
        if(request.getUser().getId().isEmpty() || request.getMajor() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            UserMajor userMajor = UserMajor.builder()
                    .user(request.getUser())
                    .major(request.getMajor())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userMajorRepository.save(userMajor);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 필수 이수 과목 생성
    public Boolean addRequiredSubject(String major, RequiredSubjectRequestDto request) throws Exception {
        // id 없음(majorGroupCode)
        if(!majorRepository.existsById(String.valueOf(major))) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        // 데이터 미입력
        if(request.getYear() == null || request.getCommon().isEmpty() || request.getAi().isEmpty() || request.getBigdata().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            RequiredSubject requiredSubject = RequiredSubject.builder()
                    .major(major)
                    .year(request.getYear())
                    .common(request.getCommon())
                    .ai(request.getAi())
                    .bigdata(request.getBigdata())
                    .build();

            requiredSubjectRepository.save(requiredSubject);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }
}
