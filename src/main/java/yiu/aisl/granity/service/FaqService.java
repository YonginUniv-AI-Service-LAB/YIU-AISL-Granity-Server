package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.Faq;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.dto.Request.FaqRequestDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.FaqRepository;
import yiu.aisl.granity.repository.MajorGroupCodeRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;
    private final MajorGroupCodeRepository majorGroupCodeRepository;

    // [API] FAQ 등록
    public Boolean registerFaq(FaqRequestDto request) throws Exception {
        // 데이터 미입력 - 400
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getMajorGroupCode() == null || request.getCategory() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // id 없음 - 404
        MajorGroupCode groupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        try {
            Faq faq = Faq.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .majorGroupCode(groupCode)
                    .category(request.getCategory())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            faqRepository.save(faq);
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

        try {
            faq.setTitle(request.getTitle());
            faq.setContents(request.getContents());
            faq.setCategory(request.getCategory());
            faq.setUpdatedAt(LocalDateTime.now());
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
