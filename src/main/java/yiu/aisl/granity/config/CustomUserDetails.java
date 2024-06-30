package yiu.aisl.granity.config;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yiu.aisl.granity.domain.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Setter
public class CustomUserDetails implements UserDetails {
    private final User user;
    private Integer role;

    public CustomUserDetails(User user, Integer role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 사용자의 역할(role)에 따라 권한을 설정합니다.
        if (role == 0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (role == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (role == 2){
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        }
        return authorities;
    }

    public CustomUserDetails(User user) {
        this.user = user;
    }
    public final User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

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
