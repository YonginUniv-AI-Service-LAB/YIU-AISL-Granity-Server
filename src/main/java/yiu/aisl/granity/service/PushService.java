package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.PushRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PushService {
    private final PushRepository pushRepository;

    // push 알림 생성(단일 사용자)
    public Boolean registerPush(Integer type, Integer typeId, User user, String contents) {
        try {
            Push push = Push.builder()
                    .user(user)
                    .type(type)
                    .type_id(typeId)
                    .contents(contents)
                    .checks(1)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            user.setPush(1);

            pushRepository.save(push);
        } catch (Exception e) {
            System.out.println("push 알림 생성 오류: " +e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // push 알림 생성(다중 사용자)
    public Boolean registerPushs(Integer type, Integer typeId, List<User> users, String contents) {
        try {
            for (User user : users) {
                Push push = Push.builder()
                        .user(user)
                        .type(type)
                        .type_id(typeId)
                        .contents(contents)
                        .checks(1)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                user.setPush(1);

                pushRepository.save(push);
            }
        } catch (Exception e) {
            System.out.println("push 알림 생성 오류: " + e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }
}
