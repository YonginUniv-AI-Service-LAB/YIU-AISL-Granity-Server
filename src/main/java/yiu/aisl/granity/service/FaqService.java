package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.Faq;
import yiu.aisl.granity.dto.FaqRequestDto;
import yiu.aisl.granity.dto.FaqResponseDto;
import yiu.aisl.granity.repository.FaqRepository;
import yiu.aisl.granity.security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    // [API] FAQ 등록
    public Boolean registerFaq(FaqRequestDto request) {
        try {
            Faq faq = Faq.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            faqRepository.save(faq);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }

    // [API] FAQ 조회
    public List<FaqResponseDto> getFaq(CustomUserDetails userDetails) {
        List<FaqResponseDto> faq = faqRepository.findAll().stream()
                .map(FaqResponseDto::new)
                .collect(Collectors.toList());

        return faq;
    }

    // [API] FAQ 삭제
    public Boolean deleteFaq(Integer id) {
        faqRepository.deleteById(id);
        return true;
    }

    // [API] FAQ 수정
    public Boolean updateFaq(Integer id, FaqRequestDto request) {
        Faq faq = faqRepository.findById(request.getId()).orElseThrow();

        faq.setTitle(request.getTitle());
        faq.setContents(request.getContents());
        faq.setUpdatedAt(LocalDateTime.now());

        faqRepository.save(faq);
        return true;
    }
}
