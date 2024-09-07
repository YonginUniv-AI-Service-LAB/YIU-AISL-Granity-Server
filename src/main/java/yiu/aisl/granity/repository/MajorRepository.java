package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, String> {
    List<Major> findByIdIn(List<String> majorId);
}
