package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorCurriculumRequestDto {
    private Integer major;
    private String name;
    private Integer classification;
    private Integer grade;
    private Integer semester;
    private Integer code;
    private Integer credit;
    private Integer theory;
    private Integer practice;
}
