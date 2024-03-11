package yiu.aisl.granity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.dto.RegisterRequestDto;
import yiu.aisl.granity.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;

    // 회원가입
    public boolean register(RegisterRequestDto request) {
        int major_id2 = Optional.ofNullable(request.getMajor_id2()).orElse(0); // 복수전공 or 부전공
        int major_id3 = Optional.ofNullable(request.getMajor_id2()).orElse(0); // 복수전공 or 부전공
        try {
            LocalDateTime createdAt = LocalDateTime.now();
            User user = User.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .major_id1(request.getMajor_id1())
                    .major_id2(major_id2)
                    .major_id3(major_id3)
                    .grade(request.getGrade())
                    .pwd(request.getPwd())
                    .role(request.getRole())
                    .status(request.getStatus())
                    .createdAt(createdAt)
                    .build();

            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }
}
