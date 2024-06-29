package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.domain.state.RoleCategory;
import yiu.aisl.granity.repository.UserRepository;
import yiu.aisl.granity.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findById(nickname).orElseThrow(
                () -> new UsernameNotFoundException("사용자가 존재하지 않습니다.")
        );
        return new CustomUserDetails(user);
    }

    @Transactional
    public UserDetails loadUserByStudentId(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("사용자가 존재하지 않습니다.")
        );
        CustomUserDetails userDetails = new CustomUserDetails(user);

        if (user.getRole().equals(RoleCategory.관리자)) {
            userDetails.setRole("ADMIN");
        } else if(user.getRole().equals(RoleCategory.학생)){
            userDetails.setRole("USER");
        } else if(user.getRole().equals(RoleCategory.조교및교수)) {
            userDetails.setRole("MANAGER");
        }
        return userDetails;
    }
}
