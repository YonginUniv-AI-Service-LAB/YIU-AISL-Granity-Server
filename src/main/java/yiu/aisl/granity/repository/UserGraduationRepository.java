package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.domain.UserGraduation;

import java.util.List;

public interface UserGraduationRepository extends JpaRepository<UserGraduation, Integer> {
    UserGraduation findByIdAndUser(Integer id, User user);
    List<UserGraduation> findByUser(User user);
}
