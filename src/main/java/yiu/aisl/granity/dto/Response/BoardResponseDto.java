package yiu.aisl.granity.dto.Response;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private String file;
    private Integer checks;
    private Integer majorGroupCodeId;
    private Integer hit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponseDto GetBoardDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getFile(),
                board.getChecks(),
                board.getMajorGroupCode().getId(),
                board.getHit(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
