package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.dto.MajorRequestDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;

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
    public boolean updateMajor(Integer majorId, MajorRequestDto request) throws Exception {
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
}
