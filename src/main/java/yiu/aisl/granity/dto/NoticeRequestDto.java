package yiu.aisl.granity.dto;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.domain.User;

@Getter
@Setter
public class NoticeRequestDto {
    private String title;
    private String contents;
    private String file;
    private User user_id;
    private Integer category;
    private Integer status;
    private MajorGroupCode majorGroupCode;
    private Integer grade1;
    private Integer grade2;
    private Integer grade3;
    private Integer grade4;
}
