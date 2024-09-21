package yiu.aisl.granity.dto.Response;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private List<FileResponseDto> files;
    private String user;
    private String name;
    private Integer grade;
    private Integer checks;
    private String majorGroupCodeId;
    private Integer hit;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponseDto GetBoardDto(Board board, List<File> files, List<Comment> comments) {
        List<FileResponseDto> fileDtos = files.stream()
                .map(file -> new FileResponseDto(
                        file.getId(),
                        file.getType(),
                        file.getTypeId(),
                        file.getOriginName(),
                        file.getSaveName(),
                        file.getSize(),
                        file.getDeleteOrNot(),
                        file.getCreatedAt()
                ))
                .collect(Collectors.toList());

        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getBoard().getId(),
                        comment.getContents(),
                        comment.getChecks(),
                        comment.getCheckUser() != null ? comment.getCheckUser().getId() : null,
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                fileDtos,
                board.getUser().getId(),
                board.getUser().getName(),
                board.getUser().getGrade(),
                board.getChecks(),
                board.getMajorGroupCode().getId(),
                board.getHit(),
                commentDtos,
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
