package yiu.aisl.granity.dto;

import lombok.*;
import yiu.aisl.granity.domain.MajorGroupCode;

@Getter
@Setter
public class MajorLabRequestDto {
    private MajorGroupCode majorGroupCode;
    private String name;
    private String description;
    private String file;
    private String link;
    private String tel;
    private String email;
}
