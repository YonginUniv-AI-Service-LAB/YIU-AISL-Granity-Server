package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.MajorGroupCode;

@Getter
@Setter
public class RequiredSubjectRequestDto {
//    private MajorGroupCode majorGroupCode;
    private Integer year;
    private String common;
    private String ai;
    private String bigdata;
}
