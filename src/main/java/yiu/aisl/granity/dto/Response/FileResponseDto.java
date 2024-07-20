package yiu.aisl.granity.dto.Response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileResponseDto {
    private Integer id;
    private Integer type;
    private Integer typeId;
    private String originName;
    private String saveName;
    private Long size;
    private Integer deleteOrNot;
    private LocalDateTime createdAt;

    public FileResponseDto(Integer id, Integer type, Integer typeId, String originName, String saveName, Long size, Integer deleteOrNot, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.typeId = typeId;
        this.originName = originName;
        this.saveName = saveName;
        this.size = size;
        this.deleteOrNot = deleteOrNot;
        this.createdAt = createdAt;
    }
}
