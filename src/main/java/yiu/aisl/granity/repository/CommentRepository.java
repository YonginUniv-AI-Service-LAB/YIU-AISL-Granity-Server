package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.*;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    Comment findByIdAndUser(Integer boardId, User user);
    List<Comment> findByUser(User user);
    List<Comment> findByBoard(Board boardId);
}
