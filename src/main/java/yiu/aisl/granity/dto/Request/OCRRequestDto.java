package yiu.aisl.granity.dto.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OCRRequestDto {
    private List<MultipartFile> files = new ArrayList<>();
}
