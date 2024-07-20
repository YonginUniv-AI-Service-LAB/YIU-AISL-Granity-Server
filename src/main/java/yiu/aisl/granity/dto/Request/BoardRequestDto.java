package yiu.aisl.granity.dto.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.domain.*;

import java.util.*;

@Getter
@Setter
public class BoardRequestDto {
    private String title;
    private String contents;
    private List<MultipartFile> files = new ArrayList<>();
//    private String file;
    private User user;
    private Integer checks;
    private MajorGroupCode majorGroupCode;
    private Integer hit;
}
