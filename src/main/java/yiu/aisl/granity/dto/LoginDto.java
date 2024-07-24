package yiu.aisl.granity.dto;

import lombok.*;
import yiu.aisl.granity.dto.TokenDto;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String id;
    private String name;
    private Integer grade;
    private Integer role;
    private Integer status;
    private Integer push;
    private TokenDto token;
}
