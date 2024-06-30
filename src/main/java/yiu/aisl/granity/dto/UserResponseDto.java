package yiu.aisl.granity.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;
    private String name;
    private Integer grade;
    private Integer role;
    private Integer status;
    private Integer push;
    private TokenDto token;
}
