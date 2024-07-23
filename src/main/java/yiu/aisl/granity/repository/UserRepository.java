package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByRole(Integer role);
}
