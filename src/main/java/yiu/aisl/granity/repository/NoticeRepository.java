package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.domain.Notice;
import yiu.aisl.granity.domain.User;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Notice findByIdAndUser(Integer id, User user);
    List<Notice> findByMajorGroupCode(MajorGroupCode id);

    List<Notice> findAllByMajorGroupCode(MajorGroupCode majorGroupCode);
}
