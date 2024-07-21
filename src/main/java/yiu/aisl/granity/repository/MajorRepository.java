package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;

public interface MajorRepository extends JpaRepository<Major, String> {
}
