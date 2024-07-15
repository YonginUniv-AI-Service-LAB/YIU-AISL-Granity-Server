package yiu.aisl.granity.dto.Request;

import lombok.*;

@Getter
@Setter
public class UserRequestDto {
    private String id;
    private String name;
    private Integer grade;
    private String pwd;
    private Integer role;
    private Integer status;
    private Integer push;
}
