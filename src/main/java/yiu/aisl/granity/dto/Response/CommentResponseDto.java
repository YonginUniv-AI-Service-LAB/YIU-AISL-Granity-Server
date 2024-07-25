package yiu.aisl.granity.dto.Response;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;

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
        String checkUserId = Optional.ofNullable(comment.getCheckUser())
                .map(User::getId)
                .map(String::valueOf)
                .orElse(null);
        return new CommentResponseDto(
                comment.getId(),
                comment.getBoard().getId(),
                comment.getContents(),
                comment.getChecks(),
                checkUserId,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
