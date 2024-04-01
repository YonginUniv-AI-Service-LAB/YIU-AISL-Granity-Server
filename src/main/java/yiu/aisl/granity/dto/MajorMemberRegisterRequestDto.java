package yiu.aisl.granity.dto;

import lombok.*;
import yiu.aisl.granity.domain.Major;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorMemberRegisterRequestDto {
    public Integer id;
    private Integer major;
    private Integer role;
    private String name;
    private String image;
    private String content1;
    private String content2;
    private String content3;
    private String tel;
    private String address;
    private String email;
}
