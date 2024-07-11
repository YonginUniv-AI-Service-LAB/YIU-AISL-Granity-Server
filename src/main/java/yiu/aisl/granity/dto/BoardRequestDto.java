package yiu.aisl.granity.dto;

import lombok.*;
import yiu.aisl.granity.domain.*;

@Getter
@Setter
public class BoardRequestDto {
    private String title;
    private String contents;
    private String file;
    private User user;
    private Integer checks;
    private MajorGroupCode majorGroupCode;
    private Integer hit;
}
