package yiu.aisl.granity.dto.Request;

import lombok.*;
import yiu.aisl.granity.domain.File;

@Getter
@Setter
public class FileRequestDto {
    private Integer type;
    private Integer typeId;
    private String originName;
    private String saveName;
    private Long size;

    @Builder
    public FileRequestDto(String originName, String saveName, Long size) {
        this.originName = originName;
        this.saveName = saveName;
        this.size = size;
    }

    // FileRequestDto를 File 엔티티로 변환
    public File toEntity() {
        return File.builder()
                .type(this.type)
                .typeId(this.typeId)
                .originName(this.originName)
                .saveName(this.saveName)
                .size(this.size)
                .deleteOrNot(0)
                .build();
    }
}
