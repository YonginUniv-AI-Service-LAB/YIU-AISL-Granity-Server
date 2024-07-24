package yiu.aisl.granity.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yiu.aisl.granity.domain.Major;
import yiu.aisl.granity.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByRole(Integer role);

    @Query("SELECT u FROM User u JOIN UserMajor um ON u.id = um.user.id WHERE u.role = :role AND um.major = :major")
    List<User> findByRoleAndMajor(@Param("role") int role, @Param("major") Major major);
}
