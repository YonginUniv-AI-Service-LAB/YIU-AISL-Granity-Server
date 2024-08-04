package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageRequestDto {
    private String title;
    private String contents;
    private User fromUserId;
    private User toUserId;
    private List<User> toUserIds;
    private LocalDateTime createdAt;
}
