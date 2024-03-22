package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Major;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProfileRequestDto {
    private String id;
    private String name;
    private Major major_id1;
    private Major major_id2;
    private Major major_id3;
    private Integer grade;
    private Integer role;
    private Integer status;
}
