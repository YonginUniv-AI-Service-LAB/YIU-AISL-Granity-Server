package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Major;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorResponseDto {
    private Integer id;
    private String address;
    private String email;
    private String fax;
    private String greetings;
    private String major;
    private String tel;

    public MajorResponseDto(Major major) {
        this.address = major.getAddress();
        this.major = major.getMajor();
        this.fax = major.getFax();
        this.tel = major.getTel();
        this.id = major.getId();
        this.greetings = major.getGreetings();
        this.email = major.getEmail();
    }
}
