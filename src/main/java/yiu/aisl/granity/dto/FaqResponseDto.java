package yiu.aisl.granity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Faq;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaqResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FaqResponseDto(Faq faq) {
        this.id = faq.getId();
        this.title = faq.getTitle();
        this.contents = faq.getContents();
        this.createdAt = faq.getCreatedAt();
        this.updatedAt = faq.getUpdatedAt();
    }
}
