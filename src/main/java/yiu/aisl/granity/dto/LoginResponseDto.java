package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String id;
    private String name;
    private Integer major_id1;
    private Integer major_id2;
    private Integer major_id3;
    private Integer grade;
    private Integer role;
    private Integer status;
    private TokenDto token;
}
