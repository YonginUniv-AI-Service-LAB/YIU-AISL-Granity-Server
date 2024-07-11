package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.*;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByIdAndUser(Integer boardId, User user);
}
