package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGraduationGroupResponseDto {
    private String userId;
    private List<UserGraduationResponseDto> graduations;
}
