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
import yiu.aisl.granity.dto.Response.*;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final FileRepository fileRepository;

    // [API] 졸업요건 처리 현황 조회
    public List<UserGraduationGroupResponseDto> getUserGraduationStatus(String id) throws Exception {
        // 특정 상태와 전공 그룹 코드를 가진 사용자 목록 조회
        List<User> users = userRepository.findByStatusAndMajorGroupCode(3, id);
        // 사용자별 졸업 요건 DTO를 저장할 맵 생성
        Map<String, List<UserGraduationResponseDto>> userGraduationMap = new HashMap<>();

        // 각 사용자에 대해 반복
        for (User user : users) {
            // 해당 사용자의 졸업 요건 목록 조회
            List<UserGraduation> userGraduations = userGraduationRepository.findByUser(user);

            // 각 졸업 요건에 대해 반복
            for (UserGraduation userGraduation : userGraduations) {
                // 졸업 요건과 관련된 파일 목록 조회
                List<File> files = fileRepository.findAllByTypeAndTypeId(6, userGraduation.getId());
                // 졸업 요건 DTO 생성
                UserGraduationResponseDto dto = UserGraduationResponseDto.GetUserGraduationDto(userGraduation, files);

                // 맵에 사용자별로 졸업 요건 DTO 추가
                userGraduationMap
                        .computeIfAbsent(user.getId(), k -> new ArrayList<>())
                        .add(dto);
            }
        }

        // 사용자별 그룹화된 응답 생성
        List<UserGraduationGroupResponseDto> groupedResponse = new ArrayList<>();
        for (Map.Entry<String, List<UserGraduationResponseDto>> entry : userGraduationMap.entrySet()) {
            groupedResponse.add(new UserGraduationGroupResponseDto(entry.getKey(), entry.getValue()));
        }

        return groupedResponse;
    }

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

    // [API] 학생 졸업 요건 삭제 (특정 학생의 특정 졸업 요건 삭제)
    public Boolean deleteStudentGraduation(Integer id) throws Exception {
        UserGraduation userGraduation = userGraduationRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        userGraduationRepository.deleteById(userGraduation.getId());
        return true;
    }

    // [API] 학생 졸업 요건 삭제 (선택된 졸업 요건 삭제)
    public Boolean deleteStudentGraduationSelected(UserGraduationRequestDto request) throws Exception {
        List<Integer> userGraduations = request.getIds();

        for(Integer userGraduation : userGraduations) {
            userGraduationRepository.deleteById(userGraduation);
        }

        return true;
    }

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

    // [API] 졸업 예정자 조회
    public List<UserResponseDto> getGraduationStudents(String id) throws Exception {
        List<User> users = userRepository.findByStatusAndMajorGroupCode(3, id);

        List<UserResponseDto> getListDto = new ArrayList<>();
        for(User user : users) {
            getListDto.add(UserResponseDto.GetUserDto(user));
        }

        return getListDto;
    }
}
