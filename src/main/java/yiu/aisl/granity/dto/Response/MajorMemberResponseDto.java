package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.File;
import yiu.aisl.granity.domain.MajorMember;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorMemberResponseDto {
    private Integer id;
    private Integer role;
    private String name;
    private List<FileResponseDto> files;
    private String content1;
    private String content2;
    private String content3;
    private String tel;
    private String address;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MajorMemberResponseDto GetMajorMemberDto(MajorMember majorMember, List<File> files) {
        List<FileResponseDto> fileDtos = files.stream()
                .map(file -> new FileResponseDto(
                        file.getId(),
                        file.getType(),
                        file.getTypeId(),
                        file.getOriginName(),
                        file.getSaveName(),
                        file.getSize(),
                        file.getDeleteOrNot(),
                        file.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new MajorMemberResponseDto(
                majorMember.getId(),
                majorMember.getRole(),
                majorMember.getName(),
                fileDtos,
                majorMember.getContent1(),
                majorMember.getContent2(),
                majorMember.getContent3(),
                majorMember.getTel(),
                majorMember.getAddress(),
                majorMember.getEmail(),
                majorMember.getCreatedAt(),
                majorMember.getUpdatedAt()
        );
    }
}
