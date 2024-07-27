package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.MajorCurriculum;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorCurriculumResponseDto {
    private Integer id;
    private String subject;
    private Integer classification;
    private Integer grade;
    private Integer semester;
    private Integer code;
    private Integer credit;
    private Integer theory;
    private Integer practice;
    private Integer hidden;
    private Integer required;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MajorCurriculumResponseDto GetMajorCurriculumDto(MajorCurriculum majorCurriculum) {
        return new MajorCurriculumResponseDto(
                majorCurriculum.getId(),
                majorCurriculum.getSubject(),
                majorCurriculum.getClassification(),
                majorCurriculum.getGrade(),
                majorCurriculum.getSemester(),
                majorCurriculum.getCode(),
                majorCurriculum.getCredit(),
                majorCurriculum.getTheory(),
                majorCurriculum.getPractice(),
                majorCurriculum.getHidden(),
                majorCurriculum.getRequired(),
                majorCurriculum.getCreatedAt(),
                majorCurriculum.getUpdatedAt()
        );
    }
}
