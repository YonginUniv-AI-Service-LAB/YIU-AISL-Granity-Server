package yiu.aisl.granity.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;

import java.util.Optional;

@Transactional
public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findById(Integer id);
}
