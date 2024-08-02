package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.Major;

@Getter
@Setter
public class MajorGraduationRequestDto {
    private Major major;
    private String title;
    private String contents;
    private Integer target;
}
