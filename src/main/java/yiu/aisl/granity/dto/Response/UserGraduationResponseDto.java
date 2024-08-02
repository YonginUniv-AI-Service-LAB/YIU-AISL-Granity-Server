package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGraduationResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private String content;
    private Integer status;
    private List<FileResponseDto> files;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserGraduationResponseDto GetUserGraduationDto(UserGraduation userGraduation, List<File> files) {
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

        return new UserGraduationResponseDto(
                userGraduation.getId(),
                userGraduation.getMajorGraduation().getTitle(),
                userGraduation.getMajorGraduation().getContents(),
                userGraduation.getContents(),
                userGraduation.getStatus(),
                fileDtos,
                userGraduation.getFeedback(),
                userGraduation.getCreatedAt(),
                userGraduation.getUpdatedAt()
        );
    }
}
