package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.Major;

@Getter
@Setter
public class MajorHistoryRequestDto {
    private Integer year;
    private Integer month;
    private String event;
    private Major major;
}
