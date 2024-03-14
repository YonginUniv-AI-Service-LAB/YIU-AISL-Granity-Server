package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import yiu.aisl.granity.domain.User;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(String id);
}
