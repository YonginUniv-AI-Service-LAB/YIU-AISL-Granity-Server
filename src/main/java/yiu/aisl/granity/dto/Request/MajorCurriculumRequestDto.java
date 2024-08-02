package yiu.aisl.granity.dto.Request;

import lombok.*;
import yiu.aisl.granity.domain.MajorGroupCode;

@Getter
@Setter
public class MajorCurriculumRequestDto {
    private MajorGroupCode majorGroupCode;
    private String subject;
    private Integer classification;
    private Integer grade;
    private Integer semester;
    private Integer code;
    private Integer credit;
    private Integer theory;
    private Integer practice;
    private String contents;
    private Integer hidden;
    private Integer required;
}
