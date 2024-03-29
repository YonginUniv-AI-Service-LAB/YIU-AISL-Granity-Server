package yiu.aisl.granity.dto;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.Major;

@Getter
@Setter
public class RegisterRequestDto {
    private String id;
    private String name;
    private Major major_id1;
    private Major major_id2;
    private Major major_id3;
    private Integer grade;
    private String pwd;
    private Integer role;
    private Integer status;
}
