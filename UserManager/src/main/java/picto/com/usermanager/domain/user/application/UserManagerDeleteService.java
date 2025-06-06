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
        return baseUrl;
    }

    @Transactional
    public void deleteUSer(UserDeleteRequest request) {
        try {
            // Keycloak에서 사용자 삭제
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(getKeycloakServerUrl())
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();

            List<UserRepresentation> users = keycloak.realm(realm).users().search(request.getAccountName(), null, null,
                    request.getEmail(),
                    0, 1);
            if (!users.isEmpty()) {
                UserRepresentation user = users.get(0);

                // 비밀번호 검증
                if (!keycloak.realm(realm).users().get(user.getId()).credentials().stream()
                        .anyMatch(credential -> credential.getValue().equals(request.getPassword()))) {
                    throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                }

                keycloak.realm(realm).users().delete(user.getId());
            }

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
