package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.*;

import java.util.Optional;

public interface PushRepository extends JpaRepository<Push, Integer> {
    Optional<Push> findByIdAndUser(Integer pushId, User user);
}
