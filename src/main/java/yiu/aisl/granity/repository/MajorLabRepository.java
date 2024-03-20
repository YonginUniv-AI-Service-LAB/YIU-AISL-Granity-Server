package yiu.aisl.granity.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorLab;

@Transactional
public interface MajorLabRepository extends JpaRepository<MajorLab, String> {
}
