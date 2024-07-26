package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.*;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByIdAndUser(Integer boardId, User user);
    List<Board> findByUser(User user);

    List<Board> findByMajorGroupCode(MajorGroupCode code);
    List<Board> findAllByMajorGroupCode(MajorGroupCode code);
}
