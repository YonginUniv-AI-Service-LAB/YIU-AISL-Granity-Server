package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Notice;
import yiu.aisl.granity.domain.User;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Notice findByIdAndUser(Integer id, User user);
}
