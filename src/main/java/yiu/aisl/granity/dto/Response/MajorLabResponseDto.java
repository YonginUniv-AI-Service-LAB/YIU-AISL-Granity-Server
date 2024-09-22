package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.File;
import yiu.aisl.granity.domain.MajorLab;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorLabResponseDto {
    private Integer id;
    private String name;
    private String description;
    List<FileResponseDto> files;
    private String link;
    private String tel;
    private String email;
    private String professor;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MajorLabResponseDto GetMajorLabDto(MajorLab majorLab, List<File> files) {
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

        return new MajorLabResponseDto(
                majorLab.getId(),
                majorLab.getName(),
                majorLab.getDescription(),
                fileDtos,
                majorLab.getLink(),
                majorLab.getTel(),
                majorLab.getEmail(),
                majorLab.getProfessor(),
                majorLab.getAddress(),
                majorLab.getCreatedAt(),
                majorLab.getUpdatedAt()
        );
    }
}
