package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
}
