package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.domain.Notice;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.dto.*;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.MajorGroupCodeRepository;
import yiu.aisl.granity.repository.NoticeRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MajorGroupCodeRepository majorGroupCodeRepository;

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

        try {
            Notice notice = Notice.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .file(request.getFile())
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
            noticeRepository.save(notice);
        } catch (Exception e) {
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

        noticeRepository.delete(notice);
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

        try {
            notice.setTitle(request.getTitle());
            notice.setContents(request.getContents());
            notice.setFile(request.getFile());
            notice.setGrade1(request.getGrade1());
            notice.setGrade2(request.getGrade2());
            notice.setGrade3(request.getGrade3());
            notice.setGrade4(request.getGrade4());
            notice.setStatus(status);
            notice.setUpdatedAt(LocalDateTime.now());
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
        notice.setStatus(1);
        return true;
    }

    // [API] 뉴스 거절하기
    public Boolean rejectionNotice(Integer noticeId) throws Exception {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));
        notice.setStatus(2);
        return true;
    }
}
