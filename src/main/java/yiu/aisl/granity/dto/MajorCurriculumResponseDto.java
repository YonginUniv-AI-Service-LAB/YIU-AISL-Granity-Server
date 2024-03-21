package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.MajorCurriculum;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorCurriculumResponseDto {
    private Integer id;
    private String major;
    private String name;
    private Integer classification;
    private Integer grade;
    private Integer semester;
    private Integer code;
    private Integer credit;
    private Integer theory;
    private Integer practice;

    public MajorCurriculumResponseDto(MajorCurriculum curriculum) {
        this.id = curriculum.getId();
        this.major = curriculum.getMajor().getMajor();
        this.name = curriculum.getName();
        this.classification = curriculum.getClassification();
        this.grade = curriculum.getGrade();
        this.semester = curriculum.getSemester();
        this.code = curriculum.getCode();
        this.credit = curriculum.getCredit();
        this.theory = curriculum.getTheory();
        this.practice = curriculum.getPractice();
    }
}
