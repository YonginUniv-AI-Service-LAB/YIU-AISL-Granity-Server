package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorGraduation;

import java.util.List;

public interface MajorGraduationRepository extends JpaRepository<MajorGraduation, Integer> {
    List<MajorGraduation> findAllByMajor(Major major);

    List<MajorGraduation> findAllByTargetAndMajor(Integer target, Major major);
}
