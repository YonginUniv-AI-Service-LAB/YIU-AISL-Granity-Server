package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorGraduation;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorGraduationResponseDto {
    private Integer id;
    private String majorId;
    private String majorName;
    private String title;
    private String contents;
    private Integer target;

    public static MajorGraduationResponseDto GetMajorGraduationDto(MajorGraduation majorGraduation) {
        return new MajorGraduationResponseDto(
                majorGraduation.getId(),
                majorGraduation.getMajor().getId(),
                majorGraduation.getMajor().getMajor(),
                majorGraduation.getTitle(),
                majorGraduation.getContents(),
                majorGraduation.getTarget()
        );
    }
}
