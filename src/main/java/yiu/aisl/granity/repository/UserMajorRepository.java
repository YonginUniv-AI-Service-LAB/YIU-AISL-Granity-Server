package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.domain.UserMajor;

import java.util.List;

public interface UserMajorRepository extends JpaRepository<UserMajor, Integer> {
    UserMajor findByUser(User user);
    List<UserMajor> findByMajor(Major major);
}
