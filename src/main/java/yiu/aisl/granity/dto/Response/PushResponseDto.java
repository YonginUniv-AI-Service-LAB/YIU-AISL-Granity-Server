package yiu.aisl.granity.dto.Response;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushResponseDto {
    private Integer id;
    private Integer type;
    private Integer type_id;
    private String contents;
    private Integer checks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PushResponseDto getPushDto(Push push) {
        return new PushResponseDto(
                push.getId(),
                push.getType(),
                push.getType_id(),
                push.getContents(),
                push.getChecks(),
                push.getCreatedAt(),
                push.getUpdatedAt()
        );
    }
}
