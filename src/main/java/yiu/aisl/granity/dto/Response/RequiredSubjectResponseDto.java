package yiu.aisl.granity.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.granity.domain.RequiredSubject;
import yiu.aisl.granity.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequiredSubjectResponseDto {
    private UserResponseDto user;
    private List<UserMajorResponseDto> major;
    private Integer year;
    private String majorGroupCode;
    private String common;
    private String ai;
    private String bigdata;

    public static RequiredSubjectResponseDto GetRequiredDto(RequiredSubject requiredSubject, User user) {
        List<UserMajorResponseDto> majorDtos = user.getUserMajors().stream()
                .map(UserMajorResponseDto::from)
                .collect(Collectors.toList());

        UserResponseDto userDto = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getGrade(),
                user.getRole(),
                user.getStatus(),
                user.getPush(),
                majorDtos
        );

        return new RequiredSubjectResponseDto(
                userDto,
                majorDtos,
                requiredSubject.getYear(),
                requiredSubject.getMajor(),
                requiredSubject.getCommon(),
                requiredSubject.getAi(),
                requiredSubject.getBigdata()
        );
    }
}
