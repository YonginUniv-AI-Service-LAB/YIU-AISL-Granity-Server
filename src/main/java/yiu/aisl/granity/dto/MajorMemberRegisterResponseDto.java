package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.MajorMember;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorMemberRegisterResponseDto {
    private Integer id;
    private String major;
    private Integer role;
    private String name;
    private String image;
    private String content1;
    private String content2;
    private String content3;
    private String tel;
    private String address;
    private String email;

    public MajorMemberRegisterResponseDto(MajorMember majorMember) {
        this.id = majorMember.getId();
        this.major = majorMember.getMajor().getMajor();
        this.role = majorMember.getRole();
        this.name = majorMember.getName();
        this.image = majorMember.getImage();
        this.content1 = majorMember.getContent1();
        this.content2 = majorMember.getContent2();
        this.content3 = majorMember.getContent3();
        this.tel = majorMember.getTel();
        this.address = majorMember.getAddress();
        this.email = majorMember.getEmail();
    }
}
