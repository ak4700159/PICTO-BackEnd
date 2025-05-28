package picto.com.usermanager.global.config;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authorization.AuthorizationDecision;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@KeycloakConfiguration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        (authorizeRequests) -> authorizeRequests
                                .requestMatchers("/user-manager/signin*", "/user-manager/signup*",
                                        "/user-manager/public*", "/user-manager/index*", "/swagger-ui/*", "/api-docs/*",
                                        "/signin*", "/signup*", "/public*", "/index*", "/api-docs", "/favicon.ico",
                                        "/user-manager/refresh-token", "/error", "/user-manager/email/*",
                                        "/user-manager/send-verify-email/*", "/user-manager/is-verify-email/*")
                                .permitAll()
                                // .requestMatchers("/public", "/swagger-ui/index.html")
                                .anyRequest()
                                .access((authentication, context) -> {
                                    log.info("Checking access for request: {}", context.getRequest().getRequestURI());
                                    if (authentication.get().isAuthenticated()) {
                                        log.info("User is authenticated: {}", authentication.get().getName());
                                        log.info("User authorities: {}", authentication.get().getAuthorities());

                                        // ROLE_USER 권한 확인
                                        boolean hasRole = authentication.get().getAuthorities().stream()
                                                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
                                        log.info("Has ROLE_USER: {}", hasRole);

                                        if (hasRole) {
                                            log.info("Access granted to user: {}", authentication.get().getName());
                                            return new AuthorizationDecision(true);
                                        } else {
                                            log.warn("Access denied to user: {} - Missing ROLE_USER",
                                                    authentication.get().getName());
                                            return new AuthorizationDecision(false);
                                        }
                                    }
                                    log.warn("User is not authenticated");
                                    return new AuthorizationDecision(false);
                                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}