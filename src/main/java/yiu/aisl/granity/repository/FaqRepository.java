package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Faq;

public interface FaqRepository extends JpaRepository<Faq, Integer> {
}
