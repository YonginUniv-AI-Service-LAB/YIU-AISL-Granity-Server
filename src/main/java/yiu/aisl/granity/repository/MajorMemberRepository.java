package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorMember;

public interface MajorMemberRepository extends JpaRepository<MajorMember, Integer> {
}
