package yiu.aisl.granity.dto;

import lombok.*;
import yiu.aisl.granity.domain.*;

@Getter
@Setter
public class MajorGroupRequestDto {
    private String majorGroup;
    private Integer code;
    private Major major;
    private Integer status;
    private String greetings;
    private String address;
    private String tel;
    private String email;
    private String fax;
    private String color;
}
