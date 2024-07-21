package yiu.aisl.granity.dto.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.domain.MajorGroupCode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MajorLabRequestDto {
    private MajorGroupCode majorGroupCode;
    private String name;
    private String description;
    private List<MultipartFile> files = new ArrayList<>();
    private List<Integer> removeFileId = new ArrayList<>();
//    private String file;
    private String link;
    private String tel;
    private String email;
}
