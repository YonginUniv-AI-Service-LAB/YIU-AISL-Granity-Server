package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorGroupCode;

public interface MajorGroupCodeRepository extends JpaRepository<MajorGroupCode, String> {
}
