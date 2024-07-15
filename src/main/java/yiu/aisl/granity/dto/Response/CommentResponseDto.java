package yiu.aisl.granity.dto.Response;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Integer id;
    private Integer boardId;
    private String contents;
    private Integer checks;
    private String checkUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponseDto GetCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getBoard().getId(),
                comment.getContents(),
                comment.getChecks(),
                comment.getCheckUser().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
