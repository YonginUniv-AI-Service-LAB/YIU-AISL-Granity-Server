package yiu.aisl.granity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String id;
    private String name;
    private Integer major_id1;
    private Integer major_id2;
    private Integer major_id3;
    private Integer grade;
    private Integer role;
    private Integer status;
}
