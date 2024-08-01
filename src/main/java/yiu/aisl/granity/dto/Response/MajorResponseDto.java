package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Major;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorResponseDto {
    private String id;
    private String major;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MajorResponseDto GetMajorDto(Major major) {
        return new MajorResponseDto(
                major.getId(),
                major.getMajor(),
                major.getCreatedAt(),
                major.getUpdatedAt()
        );
    }
}
