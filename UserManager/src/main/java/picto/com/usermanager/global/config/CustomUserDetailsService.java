package picto.com.usermanager.global.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Keycloak에서 인증된 사용자는 이미 검증된 상태이므로,
        // 기본적인 User 객체를 생성하여 반환합니다.
        return new User(username, "", new ArrayList<>());
    }
}