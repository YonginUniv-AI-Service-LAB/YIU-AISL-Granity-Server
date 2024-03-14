package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import yiu.aisl.granity.domain.User;
import yiu.aisl.granity.repository.UserRepository;
import yiu.aisl.granity.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("사용자가 존재하지 않습니다.")
        );
        return new CustomUserDetails(user);
    }

    //    @Override
    @Transactional
    public UserDetails loadUserByStudentId(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("사용자가 존재하지 않습니다.")
        );
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return userDetails;
    }
}
