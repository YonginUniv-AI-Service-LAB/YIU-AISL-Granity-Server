package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Message;
import yiu.aisl.granity.domain.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private String fromUserId;
    private String toUserId;
    private LocalDateTime createdAt;

    public static MessageResponseDto GetMessageDto(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getTitle(),
                message.getContents(),
                message.getFromUserId().getId(),
                message.getToUserId().getId(),
                message.getCreatedAt()
        );
    }
}
