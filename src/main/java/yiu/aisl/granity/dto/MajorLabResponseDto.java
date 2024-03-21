package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.MajorLab;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorLabResponseDto {
    private Integer id;
    private String major;
    private String name;
    private String description;
    private String image;
    private String link;
    private String tel;
    private String email;

    public MajorLabResponseDto (MajorLab lab) {
        this.id = lab.getId();
        this.major = lab.getMajor().getMajor();
        this.name = lab.getName();
        this.description = lab.getDescription();
        this.image = lab.getImage();
        this.link = lab.getLink();
        this.tel = lab.getTel();
        this.email = lab.getEmail();
    }
}
