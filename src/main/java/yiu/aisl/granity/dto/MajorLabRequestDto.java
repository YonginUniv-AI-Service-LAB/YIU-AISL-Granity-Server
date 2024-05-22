package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorLabRequestDto {
    public Integer id;
    private Integer major;
    private String name;
    private String description;
    private String image;
    private String link;
    private String tel;
    private String email;
}
