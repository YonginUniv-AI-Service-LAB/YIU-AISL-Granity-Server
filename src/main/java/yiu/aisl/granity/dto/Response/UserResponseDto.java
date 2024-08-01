package yiu.aisl.granity.dto.Response;

import lombok.*;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.domain.UserMajor;
import yiu.aisl.granity.dto.TokenDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;
    private String name;
    private Integer grade;
    private Integer role;
    private Integer status;
    private Integer push;
    private List<UserMajorResponseDto> majors;

    public static UserResponseDto getMyProfile(User user) {
        List<UserMajorResponseDto> majorDtos = user.getUserMajors().stream()
                .map(UserMajorResponseDto::from)
                .collect(Collectors.toList());

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .grade(user.getGrade())
                .role(user.getRole())
                .status(user.getStatus())
                .push(user.getPush())
                .majors(majorDtos)
                .build();
    }

    public static UserResponseDto GetUserDto(User user) {
        List<UserMajorResponseDto> majorDtos = user.getUserMajors().stream()
                .map(UserMajorResponseDto::from)
                .collect(Collectors.toList());

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getGrade(),
                user.getRole(),
                user.getStatus(),
                user.getPush(),
                majorDtos
        );
    }
}
