package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Board;
import yiu.aisl.granity.domain.File;
import yiu.aisl.granity.domain.Notice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private List<FileResponseDto> files;
    private String user;
    private Integer category;
    private Integer status;
    private Integer hit;
    private Integer grade1;
    private Integer grade2;
    private Integer grade3;
    private Integer grade4;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoticeResponseDto GetNoticeDto(Notice notice, List<File> files) {
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

        return new NoticeResponseDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContents(),
                fileDtos,
                notice.getUser().getId(),
                notice.getCategory(),
                notice.getStatus(),
                notice.getHit(),
                notice.getGrade1(),
                notice.getGrade2(),
                notice.getGrade3(),
                notice.getGrade4(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}
