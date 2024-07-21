package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.UserMajor;

public interface UserMajorRepository extends JpaRepository<UserMajor, Integer> {
}
