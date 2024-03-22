package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyProfileResponseDto {
    private String id;
    private String name;
    private String major_id1;
    private String major_id2;
    private String major_id3;
    private Integer grade;
    private Integer role;
    private Integer status;
}
