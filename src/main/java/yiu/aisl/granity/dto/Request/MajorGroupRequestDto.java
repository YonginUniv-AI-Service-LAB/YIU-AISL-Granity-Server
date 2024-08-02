package yiu.aisl.granity.dto.Request;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.util.List;

@Getter
@Setter
public class MajorGroupRequestDto {
    private String majorGroup;
    private List<String> codes;
    private String code;
    private Major major;
    private String greetings;
    private String address;
    private String tel;
    private String email;
    private String fax;
    private String color;
}
