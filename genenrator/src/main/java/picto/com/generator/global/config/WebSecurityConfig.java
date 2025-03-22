package picto.com.generator.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    // 스프링부트 보안인인증 무시 설정
    @Bean
    public WebSecurityCustomizer configure(){
        return (web ->
                web.ignoring()
                        .requestMatchers("/generator/user/**"));
    }
}
