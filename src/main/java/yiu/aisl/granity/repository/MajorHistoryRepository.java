package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorHistory;

import java.util.List;

public interface MajorHistoryRepository extends JpaRepository<MajorHistory, Integer> {
    List<MajorHistory> findAllByMajor(Major major);
}
