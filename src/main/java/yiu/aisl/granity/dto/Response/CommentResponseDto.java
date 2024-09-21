package yiu.aisl.granity.dto.Response;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Integer id;
    private Integer boardId;
    private UserResponseDto user;
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

        List<UserMajorResponseDto> majorDtos = comment.getUser().getUserMajors().stream()
                .map(UserMajorResponseDto::from)
                .collect(Collectors.toList());

        UserResponseDto userDto = new UserResponseDto(
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getUser().getGrade(),
                comment.getUser().getRole(),
                comment.getUser().getStatus(),
                comment.getUser().getPush(),
                majorDtos
        );

        return new CommentResponseDto(
                comment.getId(),
                comment.getBoard().getId(),
                userDto,
                comment.getContents(),
                comment.getChecks(),
                checkUserId,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
