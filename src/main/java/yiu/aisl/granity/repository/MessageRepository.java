package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Message;
import yiu.aisl.granity.domain.User;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByFromUserId(User user);
    List<Message> findByToUserId(User user);
}
