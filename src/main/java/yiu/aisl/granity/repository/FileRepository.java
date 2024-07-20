package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.File;
import yiu.aisl.granity.dto.Request.FileRequestDto;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {
//    void saveAll(List<FileRequestDto> files);
}
