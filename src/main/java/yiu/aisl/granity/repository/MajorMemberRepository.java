package yiu.aisl.granity.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.MajorMember;

@Transactional
public interface MajorMemberRepository extends JpaRepository<MajorMember, String> {

}
