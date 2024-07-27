package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorCurriculum;
import yiu.aisl.granity.domain.MajorGroupCode;

import java.util.List;

public interface MajorCurriculumRepository extends JpaRepository<MajorCurriculum, Integer> {
    List<MajorCurriculum> findByMajorGroupCode(MajorGroupCode id);
    List<MajorCurriculum> findAllByMajorGroupCode(MajorGroupCode majorGroupCode);
}
