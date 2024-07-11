package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.*;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    Comment findByIdAndUser(Integer boardId, User user);
}
