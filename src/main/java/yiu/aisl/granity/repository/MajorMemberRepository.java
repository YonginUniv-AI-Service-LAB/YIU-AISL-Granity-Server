package yiu.aisl.granity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.granity.domain.MajorGroupCode;
import yiu.aisl.granity.domain.MajorMember;

import java.util.List;

public interface MajorMemberRepository extends JpaRepository<MajorMember, Integer> {
    List<MajorMember> findByMajorGroupCode(MajorGroupCode majorGroupCode);
}
