package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.Push;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PushRepository pushRepository;

    // [API] 알림내역 조회(종 모양의 붉은 점 여부 - 쪽지 알림은 제외)

    // [API] 특정 push 알림 확인
    public Boolean pushCheck(CustomUserDetails userDetails, Integer pushId) throws Exception {
        // 권한 없음 - 403
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NO_AUTH));

        Push push = pushRepository.findByIdAndUser(pushId, user).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        push.setChecks(0);
        return true;
    }
}
