package yiu.aisl.granity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String id;
    private String pwd;
    private String name;
    private TokenDto token;
}
