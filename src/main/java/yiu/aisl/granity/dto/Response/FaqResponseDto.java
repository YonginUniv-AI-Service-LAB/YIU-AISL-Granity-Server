package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Faq;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private Integer category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FaqResponseDto GetFaqDto(Faq faq) {
        return new FaqResponseDto(
                faq.getId(),
                faq.getTitle(),
                faq.getContents(),
                faq.getCategory(),
                faq.getCreatedAt(),
                faq.getUpdatedAt()
        );
    }
}
