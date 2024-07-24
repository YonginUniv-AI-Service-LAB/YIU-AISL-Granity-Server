package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajorGroupCodeRequestDto {
    private String id;
    private String name;
    private Integer hidden;
}
