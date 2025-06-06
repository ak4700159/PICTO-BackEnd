package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.dto.request.*;
import picto.com.usermanager.domain.user.entity.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.keycloak.representations.idm.CredentialRepresentation;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserManagerPatchService {
    final UserRepository userRepository;
    private final UserSettingRepositroy userSettingRepositroy;
    private final MarkRepository markRepository;
    private final BlockRepository blockRepository;
    private final TagSelectRepositroy tagSelectRepositroy;
    private final FilterRepository filterRepository;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String baseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private String getKeycloakServerUrl() {
        return baseUrl;
    }

    @Transactional
    public void patchUser(UserRequest request) throws IllegalAccessException {
        User findUser = userRepository.getReferenceById(request.getUserId());
        if (findUser != null) {
            // Keycloak 연결 설정
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
            UserRepresentation user = users.get(0);

            // 비밀번호 검증
            if (!keycloak.realm(realm).users().get(user.getId()).credentials().stream()
                    .anyMatch(credential -> credential.getValue().equals(request.getPassword()))) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 비밀번호 변경인 경우
            if (request.getType().equals("password")) {
                findUser.setPassword(request.getPassword());

                // Keycloak 비밀번호 업데이트
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(request.getPassword());
                credential.setTemporary(false);
                keycloak.realm(realm).users().get(user.getId()).resetPassword(credential);
            }
            // 사용자 정보 변경
            else if (request.getType().equals("info")) {
                findUser.setAccountName(request.getAccountName());
                findUser.setEmail(request.getEmail());
                findUser.setName(request.getName());
                findUser.setIntro(request.getIntro());
                findUser.setProfileActive(request.getProfileActive());
                findUser.setProfilePhotoPath(request.getProfilePhotoPath());

                // Keycloak 사용자 정보 업데이트
                user.setUsername(request.getAccountName());
                user.setEmail(request.getEmail());
                user.setAttributes(Collections.singletonMap("name", Collections.singletonList(request.getName())));
                keycloak.realm(realm).users().get(user.getId()).update(user);
            }
            userRepository.save(findUser);
        } else {
            throw new IllegalAccessException("NOT FOUND USER");
        }
    }

    @Transactional
    public void patchUserSetting(SettingRequest request) throws IllegalAccessException {
        try {
            UserSetting findSetting = userSettingRepositroy.getReferenceById(request.getUserId());
            findSetting.setAroundAlert(request.isAroundAlert());
            findSetting.setLightMode(request.isLightMode());
            findSetting.setAutoRotation(request.isAutoRotation());
            findSetting.setPopularAlert(request.isPopularAlert());
            userSettingRepositroy.save(findSetting);
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }

    @Transactional
    public void patchMark(EventRequest request) throws IllegalAccessException {
        try {
            User sourceUser = userRepository.getReferenceById(request.getSourceId());
            User targetUser = userRepository.getReferenceById(request.getTargetId());
            Mark mark = new Mark(new MarkId(sourceUser.getUserId(), targetUser.getUserId()), targetUser, sourceUser);
            if (!markRepository.existsById(mark.getId())) {
                markRepository.save(mark);
            }
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }

    @Transactional
    public void patchBlock(EventRequest request) throws IllegalAccessException {
        try {
            User sourceUser = userRepository.getReferenceById(request.getSourceId());
            User targetUser = userRepository.getReferenceById(request.getTargetId());
            Block block = new Block(new BlockId(sourceUser.getUserId(), targetUser.getUserId()), targetUser,
                    sourceUser);
            if (!blockRepository.existsById(block.getId())) {
                blockRepository.save(block);
            }
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }

    // tagNames : List<String> 형식
    @Transactional
    public void patchTag(TagRequest request) throws IllegalAccessException {
        try {
            User findUser = userRepository.getReferenceById(request.getUserId());
            tagSelectRepositroy.deleteByUserId(request.getUserId());
            for (String tagName : request.getTagNames()) {
                TagSelect tag = new TagSelect(new TagSelectId(tagName, findUser.getUserId()), findUser);
                tagSelectRepositroy.save(tag);
            }
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }

    @Transactional
    public void patchFilter(FilterRequest request) throws IllegalAccessException {
        try {
            Filter filter = filterRepository.getReferenceById(request.getUserId());
            filter.setSort(request.getSort());
            filter.setPeriod(request.getPeriod());
            filterRepository.save(filter);
        } catch (Exception e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }
}
