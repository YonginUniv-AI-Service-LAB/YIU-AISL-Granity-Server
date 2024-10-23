package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.RequiredSubject;

public interface RequiredSubjectRepository extends JpaRepository<RequiredSubject, Integer> {
    RequiredSubject findByYearAndMajor(Integer year, String id);
}
