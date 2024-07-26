package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Faq;
import yiu.aisl.granity.domain.MajorGroupCode;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Integer> {
    List<Faq> findAllByMajorGroupCode(MajorGroupCode majorGroupCode);
}
