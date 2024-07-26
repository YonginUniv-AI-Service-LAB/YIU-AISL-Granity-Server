package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Request.FaqRequestDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;
    private final MajorGroupCodeRepository majorGroupCodeRepository;
    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final MajorGroupRepository majorGroupRepository;
    private final PushService pushService;

    // [API] FAQ 등록
    public Boolean registerFaq(FaqRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getMajorGroupCode() == null || request.getCategory() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<MajorGroup> majorGroups = majorGroupRepository.findByCode(groupCode.getId());
        List<Major> majors = majorGroups.stream()
                .map(majorGroup -> majorRepository.findById(majorGroup.getMajor().getId()).orElseThrow())
                .collect(Collectors.toList());

        try {
            Faq faq = Faq.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .majorGroupCode(groupCode)
                    .category(request.getCategory())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Faq mkFaq = faqRepository.save(faq);

            // 각 major에 속하는 학생들을 찾아서 중복 제거된 리스트로 결합
            Set<User> studentSet = majors.stream()
                    .flatMap(major -> userRepository.findByRoleAndMajor(1, major).stream())
                    .collect(Collectors.toSet());

            // 중복 제거된 학생들에게 푸시 알림 전송
            String pushContents = "FAQ 확인바랍니다.";
            for (User user : studentSet) {
                pushService.registerPushs(1, mkFaq.getId(), List.of(user), pushContents);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] FAQ 수정
    public Boolean updateFaq(Integer faqId, FaqRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getCategory() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        Faq faq = faqRepository.findById(faqId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        List<MajorGroup> majorGroups = majorGroupRepository.findByCode(request.getMajorGroupCode().getId());
        List<Major> majors = majorGroups.stream()
                .map(majorGroup -> majorRepository.findById(majorGroup.getMajor().getId()).orElseThrow())
                .collect(Collectors.toList());

        try {
            faq.setTitle(request.getTitle());
            faq.setContents(request.getContents());
            faq.setCategory(request.getCategory());
            faq.setUpdatedAt(LocalDateTime.now());

            // 각 major에 속하는 학생들을 찾아서 중복 제거된 리스트로 결합
            Set<User> studentSet = majors.stream()
                    .flatMap(major -> userRepository.findByRoleAndMajor(1, major).stream())
                    .collect(Collectors.toSet());

            // 중복 제거된 학생들에게 푸시 알림 전송
            String pushContents = "FAQ 확인바랍니다.";
            for (User user : studentSet) {
                pushService.registerPushs(1, faqId, List.of(user), pushContents);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }

    // [API] FAQ 삭제
    public Boolean deleteFaq(Integer faqId) throws Exception {
        // id 없음 - 404
        Faq faq =  faqRepository.findById(faqId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        faqRepository.delete(faq);
        return true;
    }
}
