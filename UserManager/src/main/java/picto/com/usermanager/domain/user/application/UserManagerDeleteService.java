package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.BlockRepository;
import picto.com.usermanager.domain.user.dao.MarkRepository;
import picto.com.usermanager.domain.user.dao.UserRepository;
import picto.com.usermanager.domain.user.dto.request.EventRequest;
import picto.com.usermanager.domain.user.dto.request.UserDeleteRequest;
import picto.com.usermanager.domain.user.entity.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;

@Service
@RequiredArgsConstructor
public class UserManagerDeleteService {
    private final UserRepository userRepository;
    private final MarkRepository markRepository;
    private final BlockRepository blockRepository;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String baseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.mail.username}")
    private String email;

    private String getKeycloakServerUrl() {
        return baseUrl.substring(0, baseUrl.indexOf("/realms/"));
    }

    private boolean verifyPasswordWithKeycloak(String email, String password) {
        String tokenUrl = baseUrl + "/protocol/openid-connect/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("username", email);
        params.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            // 200 OK면 비밀번호 일치
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            // 400 Bad Request 등은 비밀번호 불일치
            return false;
        }
    }

    @Transactional
    public void deleteUSer(UserDeleteRequest request) {
        try {
            // 1. 비밀번호 검증
            boolean isValid = verifyPasswordWithKeycloak(request.getEmail(), request.getPassword());
            if (!isValid) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 2. Keycloak 연결 및 비밀번호 변경
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(getKeycloakServerUrl())
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();

            List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null,
                    request.getEmail(),
                    0, 1);

            if (users.isEmpty()) {
                throw new IllegalArgumentException("NOT FOUND USER");
            }
            UserRepresentation user = users.get(0);
            keycloak.realm(realm).users().delete(user.getId());

            // DB에서 사용자 삭제
            userRepository.delete(userRepository.getUserByEmail(request.getEmail()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("회원 탈퇴 실패: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteMark(EventRequest request) throws IllegalAccessException {
        try {
            User sourceUser = userRepository.getReferenceById(request.getSourceId());
            User targetUser = userRepository.getReferenceById(request.getTargetId());
            markRepository.delete(
                    new Mark(new MarkId(sourceUser.getUserId(), targetUser.getUserId()), targetUser, sourceUser));
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }

    @Transactional
    public void deleteBlock(EventRequest request) throws IllegalAccessException {
        try {
            User sourceUser = userRepository.getReferenceById(request.getSourceId());
            User targetUser = userRepository.getReferenceById(request.getTargetId());
            blockRepository.delete(
                    new Block(new BlockId(sourceUser.getUserId(), targetUser.getUserId()), sourceUser, targetUser));
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }
}
