package yiu.aisl.granity.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yiu.aisl.granity.domain.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// UserDetails를 사용하면 아래의 메소드명을 변경하면 안됨
public class CustomUserDetails implements UserDetails {
    private final User user;
    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 사용자의 역할(role)에 따라 권한을 설정합니다.
        if (user.getRole() == 0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (user.getRole() == 2 || user.getRole() == 3) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    public CustomUserDetails(User user) {
        this.user = user;
    }
    public final User getUser() {
        return user;
    }

    public String getStudentId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    //이하 4개의 메소드는 jwt를 사용하기에 true로 설정
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}