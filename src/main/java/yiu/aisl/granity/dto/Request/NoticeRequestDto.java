package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.domain.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NoticeRequestDto {
    private String title;
    private String contents;
    private List<MultipartFile> files = new ArrayList<>();
    private List<Integer> removeFileId = new ArrayList<>();
//    private String file;
    private User user_id;
    private Integer category;
    private Integer status;
    private MajorGroupCode majorGroupCode;
    private Integer grade1;
    private Integer grade2;
    private Integer grade3;
    private Integer grade4;
}
