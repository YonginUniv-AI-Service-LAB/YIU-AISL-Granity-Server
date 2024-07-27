package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.domain.MajorLab;

import java.util.List;

public interface MajorLabRepository extends JpaRepository<MajorLab, Integer> {
    List<MajorLab> findByMajorGroupCode(MajorGroupCode majorGroupCode);
    List<MajorLab> findAllByMajorGroupCode(MajorGroupCode majorGroupCode);
}
