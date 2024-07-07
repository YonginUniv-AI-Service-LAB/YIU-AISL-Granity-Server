package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorGroup;

public interface MajorGroupRepository extends JpaRepository<MajorGroup, Integer> {
}
