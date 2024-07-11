package yiu.aisl.granity.dto;

import lombok.*;
import yiu.aisl.granity.domain.MajorGroupCode;

import java.time.LocalDateTime;

@Getter
@Setter
public class FaqRequestDto {
    private String title;
    private String contents;
    private MajorGroupCode majorGroupCode;
    private Integer category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
