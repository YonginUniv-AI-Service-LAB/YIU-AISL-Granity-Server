package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.controller.FileController;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Request.FileRequestDto;
import yiu.aisl.granity.dto.Request.NoticeRequestDto;
import yiu.aisl.granity.dto.Response.FileResponseDto;
import yiu.aisl.granity.dto.Response.NoticeResponseDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MajorGroupCodeRepository majorGroupCodeRepository;
    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final UserMajorRepository userMajorRepository;
    private final PushService pushService;
    private final FileController fileController;
    private final FileService fileService;
    private final FileRepository fileRepository;

    // [API] 공지 및 뉴스 조회
    public List<NoticeResponseDto> getNotices(MajorGroupCode majorGroupCode) throws Exception {
        List<Notice> notices = noticeRepository.findAllByMajorGroupCode(majorGroupCode);
        List<NoticeResponseDto> getListDto = new ArrayList<>();
        List<File> files;
        for(Notice notice : notices) {
            if(notice.getCategory() == 1) {
                files = fileRepository.findAllByTypeAndTypeId(5, notice.getId());
            } else files = fileRepository.findAllByTypeAndTypeId(1, notice.getId());
            getListDto.add(NoticeResponseDto.GetNoticeDto(notice, files));
        }
        return getListDto;
    }

    // [API] 공지 및 뉴스 등록
    public Boolean postNotice(CustomUserDetails userDetails, NoticeRequestDto request) throws Exception {
        User user =  userDetails.getUser();

        // 데이터 미입력 - 400
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getGrade1() == null || request.getGrade2() == null || request.getGrade3() == null || request.getGrade4() == null || request.getMajorGroupCode() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // if category == 0 기본 공지, else if category == 1 뉴스, else if category == 2 취업 정보
        int category = 1;
        // if status == 0 등록, else if status == 1 승인, else if status == 2 반려
        int status = 1;

        if(user.getRole() == 1) {
            category = 1;
            status = 0; // 일반 유저이기 때문에 글이 등록은 되었으나 조교, 교수 또는 관리자에게 승인을 받아야함 승인의 경우 status 가 1로 바뀜
        } else if(user.getRole() == 0 || user.getRole() == 2 || user.getRole() == 3) {
            category = request.getCategory();
            status = 1;
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<FileRequestDto> files = fileController.uploadFiles(request.getFiles());

        MajorGroupCode majorGroupCode = request.getMajorGroupCode();
        Major major = majorRepository.findById(majorGroupCode.getId()).orElseThrow();

        try {
            Notice notice = Notice.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
//                    .file(request.getFile())
                    .user(user)
                    .category(category)
                    .status(status)
                    .majorGroupCode(groupCode)
                    .grade1(request.getGrade1())
                    .grade2(request.getGrade2())
                    .grade3(request.getGrade3())
                    .grade4(request.getGrade4())
                    .hit(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            Notice mkNotice = noticeRepository.save(notice);
            fileService.saveFiles(5, mkNotice.getId(), files);
            if (user.getRole() == 1) {
                System.out.println("알림 전송 if 문 입장");
                List<User> assistant = userRepository.findByRoleAndMajor(2, major);
                List<User> professor = userRepository.findByRoleAndMajor(3, major);
                for(User users : assistant) {
                    String pushContents = "승인 대기 중인 뉴스가 있습니다. 확인해주세요.";
                    pushService.registerPushs(5, mkNotice.getId(), assistant, pushContents);
                }
                for(User users : professor) {
                    String pushContents = "승인 대기 중인 뉴스가 있습니다. 확인해주세요.";
                    pushService.registerPushs(5, mkNotice.getId(), professor, pushContents);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 공지 및 뉴스 삭제
    public Boolean deleteNotice(CustomUserDetails userDetails, Integer noticeId) throws Exception {
        User user = userDetails.getUser();
        Notice notice;
        if(user.getStatus() == 1) {
            notice = noticeRepository.findByIdAndUser(noticeId, user);
            if(notice == null) {
                throw new CustomException(ErrorCode.NOT_EXIST_ID);
            }
        } else notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(5, noticeId);

        noticeRepository.delete(notice);
        fileController.deleteFiles(deleteFiles);
        fileService.deleteAllFileByTypeAndTypeId(5, noticeId); // DB 삭제
        return true;
    }

    // [API] 공지 및 뉴스 수정
    public Boolean updateNotice(CustomUserDetails userDetails, Integer noticeId, NoticeRequestDto request) throws Exception {
        User user = userDetails.getUser();
        Notice notice;

        // if category == 0 기본 공지, else if category == 1 뉴스, else if category == 2 취업 정보
        int category = 1;
        // if status == 0 등록, else if status == 1 승인, else if status == 2 반려
        int status = 1;

        if(user.getStatus() == 1) {
            category = 1;
            status = 0; // 일반 유저이기 때문에 글이 등록은 되었으나 조교, 교수 또는 관리자에게 승인을 받아야함 승인의 경우 status 가 1로 바뀜
            notice = noticeRepository.findByIdAndUser(noticeId, user);
            if(notice == null) {
                throw new CustomException(ErrorCode.NOT_EXIST_ID);
            }
        } else {
            category = request.getCategory();
            status = 1;
            notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_EXIST_ID));
        }

        // 데이터 미입력 - 400
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getGrade1() == null || request.getGrade2() == null || request.getGrade3() == null || request.getGrade4() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        MajorGroupCode majorGroupCode = request.getMajorGroupCode();
        Major major = majorRepository.findById(majorGroupCode.getId()).orElseThrow();

        try {
            notice.setTitle(request.getTitle());
            notice.setContents(request.getContents());
            notice.setGrade1(request.getGrade1());
            notice.setGrade2(request.getGrade2());
            notice.setGrade3(request.getGrade3());
            notice.setGrade4(request.getGrade4());
            notice.setStatus(status);
            notice.setUpdatedAt(LocalDateTime.now());

            // 기존 파일 목록 조회
            List<FileResponseDto> existingFiles = fileService.findAllFileByTypeAndTypeId(5, noticeId);

            // 삭제할 파일 정보 조회
            List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(5, noticeId);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);
            fileService.deleteAllFileByTypeAndTypeId(5, noticeId); // DB 삭제

            // 파일 업로드
            List<FileRequestDto> uploadFiles = fileController.uploadFiles(request.getFiles());

            // 파일 정보 저장
            fileService.saveFiles(5, noticeId, uploadFiles);

            if (user.getRole() == 1) {
                List<User> assistant = userRepository.findByRoleAndMajor(2, major);
                List<User> professor = userRepository.findByRoleAndMajor(3, major);
                for(User users : assistant) {
                    String pushContents = "승인 대기 중인 뉴스가 있습니다. 확인해주세요.";
                    pushService.registerPushs(5, noticeId, assistant, pushContents);
                }
                for(User users : professor) {
                    String pushContents = "승인 대기 중인 뉴스가 있습니다. 확인해주세요.";
                    pushService.registerPushs(5, noticeId, professor, pushContents);
                }
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }

    // [API] 뉴스 승인하기
    public Boolean approvalNotice(Integer noticeId) throws Exception {
        // id 없음 - 404
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));
        String pushContents = "작성한 뉴스가 승인되었습니다.";
        pushService.registerPush(5, noticeId, notice.getUser(), pushContents);
        notice.setStatus(1);
        return true;
    }

    // [API] 뉴스 거절하기
    public Boolean rejectionNotice(Integer noticeId) throws Exception {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));
        String pushContents = "작성한 뉴스가 승인되었습니다.";
        pushService.registerPush(5, noticeId, notice.getUser(), pushContents);
        notice.setStatus(2);
        return true;
    }
}
