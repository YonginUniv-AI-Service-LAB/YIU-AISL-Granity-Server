package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorMember;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.dto.MajorMemberRegisterRequestDto;
import yiu.aisl.granity.repository.MajorMemberRepository;
import yiu.aisl.granity.repository.MajorRepository;
import yiu.aisl.granity.repository.UserRepository;
import yiu.aisl.granity.security.CustomUserDetails;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MajorService {
    private final UserRepository userRepository;
    private final MajorMemberRepository majorMemberRepository;
    private final MajorRepository majorRepository;

    // [API] 교수님 등록
    public Boolean registerProfessor(CustomUserDetails userDetails, MajorMemberRegisterRequestDto request) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        if(user.getRole() != 3) {
            new Exception("작업 권한 없음");
        }
        Major major = majorRepository.findById(request.getMajor()).orElseThrow();

        try {
            MajorMember professor = MajorMember.builder()
                    .major(major)
                    .role(request.getRole())
                    .name(request.getName())
                    .image(request.getImage())
                    .content1(request.getContent1())
                    .content2(request.getContent2())
                    .content3(request.getContent3())
                    .tel(request.getTel())
                    .address(request.getAddress())
                    .email(request.getEmail())
                    .build();

            majorMemberRepository.save(professor);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return true;
    }
}
