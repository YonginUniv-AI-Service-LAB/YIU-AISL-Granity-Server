package yiu.aisl.granity.dto;

import lombok.*;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class MajorMemberRequestDto {
    private MajorGroup majorGroup;
    private MajorGroupCode majorGroupCode;
    private Integer role;
    private String name;
    private String file;
    private String content1;
    private String content2;
    private String content3;
    private String tel;
    private String address;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
