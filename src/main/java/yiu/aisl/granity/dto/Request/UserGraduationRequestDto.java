package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserGraduationRequestDto {
    private List<User> users;
//    private User user;
    private Integer target;
    private Integer status;
    private String contents;
    private List<MultipartFile> files = new ArrayList<>();
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
