package yiu.aisl.granity.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorCurriculum;

@Transactional
public interface MajorCurriculumRepository extends JpaRepository<MajorCurriculum, String> {
    MajorCurriculum findById(Integer id);
}
