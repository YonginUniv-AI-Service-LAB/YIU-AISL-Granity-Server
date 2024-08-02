package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.controller.FileController;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Request.FileRequestDto;
import yiu.aisl.granity.dto.Request.MajorGraduationRequestDto;
import yiu.aisl.granity.dto.Request.UserGraduationRequestDto;
import yiu.aisl.granity.dto.Response.FaqResponseDto;
import yiu.aisl.granity.dto.Response.FileResponseDto;
import yiu.aisl.granity.dto.Response.MajorGraduationResponseDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.MajorGraduationRepository;
import yiu.aisl.granity.repository.MajorRepository;
import yiu.aisl.granity.repository.UserGraduationRepository;
import yiu.aisl.granity.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GraduationService {
    private final MajorRepository majorRepository;
    private final MajorGraduationRepository majorGraduationRepository;
    private final UserGraduationRepository userGraduationRepository;
    private final UserRepository userRepository;
    private final PushService pushService;
    private final FileController fileController;
    private final FileService fileService;

    // [API] 졸업요건 조회
    public List<MajorGraduationResponseDto> getMajorGraduations(Major id) throws Exception {
        List<MajorGraduation> majorGraduations = majorGraduationRepository.findAllByMajor(id);

        List<MajorGraduationResponseDto> getListDto = new ArrayList<>();
        for(MajorGraduation majorGraduation : majorGraduations) {
            getListDto.add(MajorGraduationResponseDto.GetMajorGraduationDto(majorGraduation));
        }

        return getListDto;
    }

    // [API] 졸업요건 등록
    public Boolean registerGraduationRequirement(MajorGraduationRequestDto request) throws Exception {
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getTarget() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        if(!majorRepository.existsById(request.getMajor().getId())) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        try {
            MajorGraduation majorGraduation = MajorGraduation.builder()
                    .major(request.getMajor())
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .target(request.getTarget())
                    .build();

            majorGraduationRepository.save(majorGraduation);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 졸업요건 삭제
    public Boolean deleteMajorGraduation(Integer id) throws Exception {
        if(!majorGraduationRepository.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        majorGraduationRepository.deleteById(id);
        return true;
    }

    // [API] 졸업요건 수정
    public Boolean updateMajorGraduation(Integer id, MajorGraduationRequestDto request) throws Exception {
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getTarget() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        MajorGraduation majorGraduation = majorGraduationRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        try {
            majorGraduation.setTitle(request.getTitle());
            majorGraduation.setContents(request.getContents());
            majorGraduation.setTarget(request.getTarget());
            majorGraduation.setMajor(request.getMajor());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 학생 졸업 요건 등록
    public Boolean registerStudentGraduation(UserGraduationRequestDto request) throws Exception {
        if(request.getUsers() == null || request.getTarget() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        List<User> users = request.getUsers();
        List<MajorGraduation> majorGraduations = majorGraduationRepository.findAllByTarget(request.getTarget());

        try {
            for(User user : users) {
                for(MajorGraduation majorGraduation : majorGraduations) {
                    UserGraduation userGraduation = UserGraduation.builder()
                            .user(user)
                            .majorGraduation(majorGraduation)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    userGraduationRepository.save(userGraduation);
                }
            }
            String pushSentence = "졸업 요건이 등록되었습니다. 확인해주세요.";
            pushService.registerPushs(6, 1, users, pushSentence);

        } catch (Exception e) {
            System.out.println(e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 학생 졸업 요건 삭제
//    public Boolean deleteStudentGraduation(UserGraduationRequestDto request) throws Exception {
//
//    }

    // [API] 졸업 요건 제출
    public Boolean applyGraduation(CustomUserDetails userDetails, Integer id, Major major, UserGraduationRequestDto request) throws Exception {
        User user = userDetails.getUser();

        if(request.getContents().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        UserGraduation userGraduation = userGraduationRepository.findByIdAndUser(id, user);

        if(userGraduation == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        try {
            userGraduation.setContents(request.getContents());
            userGraduation.setUpdatedAt(LocalDateTime.now());

            // 업로드할 파일 정보
            List<FileRequestDto> files = fileController.uploadFiles(request.getFiles());

            // 삭제할 파일 정보 조회
            List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(6, id);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);
            fileService.deleteAllFileByTypeAndTypeId(6, id); // DB 삭제

            // 파일 정보 저장
            fileService.saveFiles(6, id, files);
            List<User> assistant = userRepository.findByRoleAndMajor(2, major);
            pushService.registerPushs(6, userGraduation.getId(), assistant, "졸업 요건 승인 요청이 들어왔습니다. 확인해주세요.");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 졸업 요건 승인
    public Boolean approvalGraduation(Integer id) throws Exception {
        UserGraduation userGraduation = userGraduationRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        userGraduation.setStatus(1);
        userGraduation.setUpdatedAt(LocalDateTime.now());
        pushService.registerPush(6, userGraduation.getId(), userGraduation.getUser(), "제출한 졸업 요건이 승인되었습니다.");
        return true;
    }

    // [API] 졸업 요건 거절
    public Boolean rejectionGraduation(Integer id, UserGraduationRequestDto request) throws Exception {
        UserGraduation userGraduation = userGraduationRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        if(request.getFeedback().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        userGraduation.setStatus(2);
        userGraduation.setUpdatedAt(LocalDateTime.now());
        userGraduation.setFeedback(request.getFeedback());

        pushService.registerPush(6, userGraduation.getId(), userGraduation.getUser(), "제출한 졸업 요건이 반려되었습니다. 피드백 확인 후 다시 제출해주세요.");
        return true;
    }
}
