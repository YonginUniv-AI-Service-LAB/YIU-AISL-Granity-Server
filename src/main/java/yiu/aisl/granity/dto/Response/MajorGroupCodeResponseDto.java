package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.MajorGroupCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorGroupCodeResponseDto {
    private String id;
    private String name;
    private Integer hidden;

    public static MajorGroupCodeResponseDto GetMajorGroupCodeDto(MajorGroupCode code) {
        return new MajorGroupCodeResponseDto(
                code.getId(),
                code.getName(),
                code.getHidden()
        );
    }
}
