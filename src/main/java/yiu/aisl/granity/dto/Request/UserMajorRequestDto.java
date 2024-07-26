package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.User;

@Getter
@Setter
public class UserMajorRequestDto {
    private User user;
    private Major major;
}
