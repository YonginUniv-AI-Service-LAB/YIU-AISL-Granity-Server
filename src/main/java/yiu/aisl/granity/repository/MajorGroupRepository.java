package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorGroup;

import java.util.List;

public interface MajorGroupRepository extends JpaRepository<MajorGroup, String> {
    MajorGroup findByMajor(Major major);
}
