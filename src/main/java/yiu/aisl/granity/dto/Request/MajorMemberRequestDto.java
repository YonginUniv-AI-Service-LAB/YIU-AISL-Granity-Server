package yiu.aisl.granity.dto.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MajorMemberRequestDto {
    private Integer id;
    private MajorGroup majorGroup;
    private MajorGroupCode majorGroupCode;
    private Integer role;
    private String name;
    private List<MultipartFile> files = new ArrayList<>();
    private List<Integer> removeFileId = new ArrayList<>();
//    private String file;
    private String content1;
    private String content2;
    private String content3;
    private String tel;
    private String address;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
