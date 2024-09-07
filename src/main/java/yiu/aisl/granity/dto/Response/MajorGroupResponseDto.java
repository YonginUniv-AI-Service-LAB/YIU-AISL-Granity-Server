package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorGroup;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorGroupResponseDto {
    private String majorGroup;
    private List<MajorResponseDto> codes;
    private String code;
    private Major major;
    private String greetings;
    private String address;
    private String tel;
    private String email;
    private String fax;
    private String color;

    public static MajorGroupResponseDto GetMajorGroupDto(MajorGroup majorGroup, List<Major> majors) {
        List<MajorResponseDto> majorDtos = majors.stream()
                .map(major -> new MajorResponseDto(
                        major.getId(),
                        major.getMajor(),
                        major.getCreatedAt(),
                        major.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        return new MajorGroupResponseDto(
                majorGroup.getMajorGroup(),
                majorDtos,
                majorGroup.getCode(),
                majorGroup.getMajor(),
                majorGroup.getGreetings(),
                majorGroup.getAddress(),
                majorGroup.getTel(),
                majorGroup.getEmail(),
                majorGroup.getFax(),
                majorGroup.getColor()
        );
    }
}
