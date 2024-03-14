package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.dto.MyProfileDto;
import yiu.aisl.granity.repository.UserRepository;
import yiu.aisl.granity.security.CustomUserDetails;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // [API] 내 정보 조회
    public Object getMyProfile(CustomUserDetails userDetails) {
        Optional<User> user = userRepository.findById(userDetails.getUser().getId());
        return MyProfileDto.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .major_id1(user.get().getMajor_id1())
                .major_id2(user.get().getMajor_id2())
                .major_id3(user.get().getMajor_id3())
                .grade(user.get().getGrade())
                .role(user.get().getRole())
                .status(user.get().getStatus())
                .build();
    }
}
