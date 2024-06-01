package yiu.aisl.granity.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Faq;

@Transactional
public interface FaqRepository extends JpaRepository<Faq, Integer> {
}
