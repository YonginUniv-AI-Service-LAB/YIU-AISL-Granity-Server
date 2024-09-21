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
    private UserResponseDto user;
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

        List<UserMajorResponseDto> majorDtos = board.getUser().getUserMajors().stream()
                .map(UserMajorResponseDto::from)
                .collect(Collectors.toList());

        UserResponseDto userDto = new UserResponseDto(
                board.getUser().getId(),
                board.getUser().getName(),
                board.getUser().getGrade(),
                board.getUser().getRole(),
                board.getUser().getStatus(),
                board.getUser().getPush(),
                majorDtos
        );

        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> {
                    // checkUserId 처리
                    String checkUserId = comment.getCheckUser() != null ? comment.getCheckUser().getId().toString() : null;

                    // UserMajorResponseDto 리스트 생성
                    List<UserMajorResponseDto> commentUserMajorDtos = comment.getUser().getUserMajors().stream()
                            .map(UserMajorResponseDto::from)
                            .collect(Collectors.toList());

                    // UserResponseDto 생성
                    UserResponseDto commentUserDto = new UserResponseDto(
                            comment.getUser().getId(),
                            comment.getUser().getName(),
                            comment.getUser().getGrade(),
                            comment.getUser().getRole(),
                            comment.getUser().getStatus(),
                            comment.getUser().getPush(),
                            commentUserMajorDtos
                    );

                    // CommentResponseDto 반환
                    return new CommentResponseDto(
                            comment.getId(),
                            comment.getBoard().getId(),
                            commentUserDto,
                            comment.getContents(),
                            comment.getChecks(),
                            checkUserId,
                            comment.getCreatedAt(),
                            comment.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());

        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                fileDtos,
                userDto,
                board.getChecks(),
                board.getMajorGroupCode().getId(),
                board.getHit(),
                commentDtos,
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
