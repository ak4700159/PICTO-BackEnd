package picto.com.usermanager.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.time.Instant;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    @Value("${keycloak.realm}")
    private String realm;

    private final UserDetailsService userDetailsService;
    private PublicKey publicKey;
    private final RestTemplate restTemplate;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // permitAll 엔드포인트들은 토큰 검증 건너뛰기
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/user-manager/signin") ||
                requestURI.startsWith("/user-manager/signup") ||
                requestURI.startsWith("/user-manager/public") ||
                requestURI.startsWith("/user-manager/index") ||
                requestURI.startsWith("/signin") ||
                requestURI.startsWith("/signup") ||
                requestURI.startsWith("/public") ||
                requestURI.startsWith("/index") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/api-docs") ||
                requestURI.equals("/favicon.ico") ||
                requestURI.equals("/error") ||
                requestURI.startsWith("/user-manager/refresh-token") ||
                requestURI.startsWith("/user-manager/email")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);
            log.info("Processing request to: {}", requestURI);

            if (jwt != null) {
                log.info("Found JWT token in request");
                AccessToken token = verifyToken(jwt);
                String username = token.getPreferredUsername();

                log.info("Token verification successful for user: {}", username);
                log.info("Token details - Issued at: {}, Expires at: {}, Current time: {}",
                        token.getIssuedAt(), token.getExpiration(), Instant.now().getEpochSecond());

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.info("Loading user details for: {}", username);
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    // 토큰에서 모든 역할 정보 가져오기
                    Set<String> roles = new HashSet<>();

                    // Realm roles
                    if (token.getRealmAccess() != null) {
                        roles.addAll(token.getRealmAccess().getRoles());
                        log.info("Realm roles: {}", token.getRealmAccess().getRoles());
                    }

                    // Client roles
                    if (token.getResourceAccess() != null) {
                        token.getResourceAccess().forEach((clientId, access) -> {
                            if (access.getRoles() != null) {
                                roles.addAll(access.getRoles());
                                log.info("Client roles for {}: {}", clientId, access.getRoles());
                            }
                        });
                    }

                    log.info("All roles from token: {}", roles);

                    List<GrantedAuthority> authorities = roles.stream()
                            .map(role -> {
                                // 이미 'ROLE_' 접두사가 있는 경우 그대로 사용
                                String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                                log.info("Converting role: {} to authority: {}", role, roleWithPrefix);
                                return new SimpleGrantedAuthority(roleWithPrefix);
                            })
                            .collect(Collectors.toList());

                    log.info("Final authorities for user: {}", authorities);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Authentication completed. User: {}, Authorities: {}",
                            authentication.getName(),
                            authentication.getAuthorities());

                    // 인증 성공 메시지를 응답 헤더에 추가
                    response.setHeader("X-Auth-Status", "success");
                    response.setHeader("X-Auth-User", authentication.getName());

                } else {
                    log.warn("Username is null or user is already authenticated");
                }
            } else {
                log.warn("No JWT token found in request");
            }
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // 클라이언트에게 오류 메시지 전달
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null) {
            // 'Bearer ' 접두사가 있으면 제거하고, 없으면 그대로 반환
            return bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        }
        return null;
    }

    private AccessToken verifyToken(String token) throws VerificationException {
        try {
            if (publicKey == null) {
                log.info("Loading public key...");
                loadPublicKey();
            }

            log.info("Creating token verifier");
            TokenVerifier<AccessToken> verifier = TokenVerifier.create(token, AccessToken.class)
                    .withChecks(TokenVerifier.IS_ACTIVE)
                    .publicKey(publicKey);

            log.info("Verifying token");
            AccessToken accessToken = verifier.verify().getToken();
            log.info("Token verified successfully");

            if (!accessToken.isActive()) {
                log.error("Token is not active - Issued at: {}, Expires at: {}, Current time: {}",
                        accessToken.getIssuedAt(), accessToken.getExpiration(), Instant.now().getEpochSecond());
                throw new VerificationException("만료된 토큰입니다.");
            }

            // 만료 시간 체크
            if (accessToken.getExpiration() < Instant.now().getEpochSecond()) {
                log.error("Token has expired - Expires at: {}, Current time: {}",
                        accessToken.getExpiration(), Instant.now().getEpochSecond());
                throw new VerificationException("만료된 토큰입니다.");
            }

            // 역할 정보 로깅
            if (accessToken.getRealmAccess() != null) {
                log.info("Realm access roles: {}", accessToken.getRealmAccess().getRoles());
            } else {
                log.warn("No realm access roles found in token");
            }

            return accessToken;
        } catch (Exception e) {
            log.error("Token verification failed: {}", e.getMessage(), e);
            if (e.getMessage().contains("expired") || e.getMessage().contains("만료된")) {
                throw new VerificationException("만료된 토큰입니다.");
            }
            throw new VerificationException("Invalid token", e);
        }
    }

    private void loadPublicKey() throws Exception {
        String certsUrl = issuerUri + "/protocol/openid-connect/certs";
        log.debug("Loading public key from: {}", certsUrl);

        ResponseEntity<String> response = restTemplate.exchange(
                certsUrl,
                HttpMethod.GET,
                null,
                String.class);

        if (response.getBody() == null) {
            throw new Exception("Failed to get certificates from Keycloak");
        }

        String cert = extractCertificateFromResponse(response.getBody());
        log.debug("Extracted certificate from response");

        byte[] certBytes = Base64.getDecoder().decode(cert);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
        publicKey = certificate.getPublicKey();
        log.debug("Public key loaded successfully");
    }

    private String extractCertificateFromResponse(String response) {
        try {
            int startIndex = response.indexOf("\"x5c\":[\"") + 8;
            int endIndex = response.indexOf("\"", startIndex);
            if (startIndex == -1 || endIndex == -1) {
                throw new Exception("Invalid certificate response format");
            }
            return response.substring(startIndex, endIndex);
        } catch (Exception e) {
            log.error("Failed to extract certificate from response: {}", e.getMessage());
            throw new RuntimeException("Failed to extract certificate", e);
        }
    }
}