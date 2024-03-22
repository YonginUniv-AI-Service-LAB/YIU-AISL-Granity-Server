package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorStudentResponseDto {
    private String id;
    private String name;
    private Integer grade;
    private String major1;
    private String major2;
    private String major3;
    private Integer status;

    public MajorStudentResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.grade = user.getGrade();
        this.major1 = user.getMajor_id1().getMajor();
        this.major2 = user.getMajor_id2().getMajor();
        this.major3 = user.getMajor_id3().getMajor();
        this.status = user.getStatus();
    }
}
