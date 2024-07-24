package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMajorResponseDto {
    private String major;

    public static UserMajorResponseDto from(UserMajor userMajor) {
        return new UserMajorResponseDto(userMajor.getMajor().getMajor());
    }
}
