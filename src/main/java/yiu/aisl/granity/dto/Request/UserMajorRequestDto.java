package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.Major;

@Getter
@Setter
public class UserMajorRequestDto {
    private Major major;
}
