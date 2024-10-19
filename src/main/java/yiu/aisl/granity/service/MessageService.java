package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.Message;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.dto.Request.MessageRequestDto;
import yiu.aisl.granity.dto.Response.MessageResponseDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.MessageRepository;
import yiu.aisl.granity.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.reverse;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final PushService pushService;

    // [API] 쪽지 조회
    public List<MessageResponseDto> getMessages(CustomUserDetails userDetails) throws Exception {
        List<Message> messages;
        if(userDetails.getUser().getRole() == 1) {
            messages = messageRepository.findByToUserId(userDetails.getUser());
        } else messages = messageRepository.findByFromUserId(userDetails.getUser());

        List<MessageResponseDto> getListDto = new ArrayList<>();
        for(Message message : messages) {
            getListDto.add(MessageResponseDto.GetMessageDto(message));
        }

        return reverse(getListDto);
    }

    // [API] 쪽지 보내기
    public Boolean sendMessage(CustomUserDetails userDetails, MessageRequestDto request) throws Exception {
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getToUserId() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        if(!userRepository.existsById(request.getToUserId().getId())) {
            throw new CustomException(ErrorCode.NOT_EXIST_MEMBER);
        }

        User fromUserId = userDetails.getUser();
        User toUserId = request.getToUserId();

        try {
            Message message = Message.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .fromUserId(fromUserId)
                    .toUserId(toUserId)
                    .createdAt(LocalDateTime.now())
                    .build();

            Message mkMessage = messageRepository.save(message);

            String pushMessage = "쪽지가 왔습니다. 확인해주세요.";
            pushService.registerPush(2, mkMessage.getId(), toUserId, pushMessage);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 쪽지 보내기(다중)
    public Boolean sendMessages(CustomUserDetails userDetails, MessageRequestDto request) throws Exception {
        if(request.getTitle().isEmpty() || request.getContents().isEmpty() || request.getToUserIds() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            User toUserId;
            for(User user : request.getToUserIds()) {
                toUserId = user;
                if(!userRepository.existsById(toUserId.getId())) {
                    throw new CustomException(ErrorCode.NOT_EXIST_MEMBER);
                }

                Message message = Message.builder()
                        .title(request.getTitle())
                        .contents(request.getContents())
                        .fromUserId(userDetails.getUser())
                        .toUserId(toUserId)
                        .createdAt(LocalDateTime.now())
                        .build();

                Message mkMessage = messageRepository.save(message);

                String pushMessage = "쪽지가 왔습니다. 확인해주세요.";
                pushService.registerPush(2, mkMessage.getId(), toUserId, pushMessage);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }
}
