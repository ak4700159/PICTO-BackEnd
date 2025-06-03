package picto.com.usermanager.domain.user.application;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.AccessTokenResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.dto.request.SignInRequest;
import picto.com.usermanager.domain.user.dto.request.SignUpRequest;
import picto.com.usermanager.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.usermanager.domain.user.dto.response.SignInResponse;
import picto.com.usermanager.domain.user.entity.*;
import picto.com.usermanager.global.utils.KakaoUtil;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagerLoginService {

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String baseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private final KakaoUtil kakaoUtil;
    // 기본 세팅을 위한 레파지토리
    private final UserRepository userRepository;
    private final FilterRepository filterRepository;
    private final UserSettingRepositroy userSettingRepository;
    private final TagSelectRepositroy tagSelectRepositroy;
    private final SessionRepository sessionRepository;

    private String getKeycloakServerUrl() {
        return baseUrl.substring(0, baseUrl.indexOf("/realms/"));
    }

    // 사용자 디폴트값 설정
    @Transactional
    public void addDefaultUser(User newUser, SignUpRequest signUpRequest) throws IllegalAccessException {
        User referUser = userRepository.getUserByEmail(newUser.getEmail());
        Filter defualFilter = Filter.toEntity(referUser);
        UserSetting defaultSetting = UserSetting.toEntity(referUser);
        TagSelect defaultTag = TagSelect.toEntity(referUser, "돼지");

        double lat = signUpRequest.getLat();
        double lng = signUpRequest.getLng();
        String location;
        GetKakaoLocationInfoResponse response = kakaoUtil.convertLocationFromPos(lat, lng);
        if (Objects.requireNonNull(response).getDocuments().isEmpty()) {
            location = "좌표 식별 불가";
        } else {
            location = response.getDocuments().get(0).getAddress().getAddress_name();
        }
        Session defaultSession = Session.toEntity(referUser, lat, lng, location);

        try {
            filterRepository.save(defualFilter);
            userSettingRepository.save(defaultSetting);
            tagSelectRepositroy.save(defaultTag);
            sessionRepository.save(defaultSession);
        } catch (IllegalArgumentException e) {
            System.out.println("Default Setting Error");
            throw new IllegalAccessException();
        }
    }

    @Transactional
    public User signUp(SignUpRequest signUpRequest) throws IllegalAccessException {
        String userId = null;
        try {
            // Keycloak에 user 생성
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(getKeycloakServerUrl())
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();

            UserRepresentation user = new UserRepresentation();
            user.setUsername(signUpRequest.getAccountName());
            user.setEmail(signUpRequest.getEmail());
            user.setAttributes(Collections.singletonMap("name",
                    Collections.singletonList(signUpRequest.getName())));
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(signUpRequest.getPassword());
            user.setCredentials(Collections.singletonList(credential));

            // 사용자 생성
            Response response = keycloak.realm(realm).users().create(user);

            if (response.getStatus() == 201) {
                try {
                    // 생성된 사용자의 ID 가져오기
                    userId = response.getLocation().getPath()
                            .substring(response.getLocation().getPath().lastIndexOf('/') + 1);
                    log.info("Created user with ID: {}", userId);

                    // 모든 클라이언트 목록 가져오기
                    var allClients = keycloak.realm(realm).clients().findAll();
                    log.info("All available clients: {}",
                            allClients.stream()
                                    .map(c -> String.format("ID: %s, ClientId: %s", c.getId(), c.getClientId()))
                                    .collect(Collectors.joining(", ")));

                    // 클라이언트 ID 확인
                    log.info("Using client ID: {}", clientId);

                    // 클라이언트 목록에서 clientId로 찾기
                    var clients = keycloak.realm(realm).clients().findByClientId(clientId);
                    if (clients.isEmpty()) {
                        throw new RuntimeException("Client not found with ID: " + clientId);
                    }

                    var client = clients.get(0);
                    log.info("Found client with ID: {} and name: {}", client.getId(), client.getClientId());

                    // 클라이언트의 역할 목록 가져오기
                    try {
                        var roles = keycloak.realm(realm).clients().get(client.getId()).roles().list();
                        log.info("Available client roles: {}",
                                roles.stream().map(r -> r.getName()).collect(Collectors.joining(", ")));

                        // ROLE_USER 역할 찾기
                        var roleRepresentation = roles.stream()
                                .filter(role -> role.getName().equals("ROLE_USER"))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("ROLE_USER role not found in client roles"));

                        log.info("Found ROLE_USER role: {}", roleRepresentation.getName());

                        // 사용자에게 클라이언트 레벨 역할 할당
                        try {
                            keycloak.realm(realm).users().get(userId).roles().clientLevel(client.getId())
                                    .add(Collections.singletonList(roleRepresentation));
                            log.info("Successfully assigned ROLE_USER role to user: {}",
                                    signUpRequest.getName());
                        } catch (Exception e) {
                            log.error("Failed to assign role to user: {} - Error: {}", e.getMessage(), e);
                            throw new RuntimeException("Failed to assign role to user: " + e.getMessage(), e);
                        }
                    } catch (Exception e) {
                        log.error("Failed to get or assign client roles: {} - Error: {}", e.getMessage(), e);
                        throw new RuntimeException("Failed to get or assign client roles: " + e.getMessage(), e);
                    }
                } catch (Exception e) {
                    log.error("Failed in post-user creation process: {} - Error: {}", e.getMessage(), e);
                    // 역할 할당 실패 시 사용자 삭제
                    if (userId != null) {
                        try {
                            keycloak.realm(realm).users().delete(userId);
                            log.info("Deleted user {} after role assignment failure", userId);
                        } catch (Exception deleteError) {
                            log.error("Failed to delete user after role assignment failure: {}",
                                    deleteError.getMessage());
                        }
                    }
                    throw new RuntimeException("Failed in post-user creation process: " + e.getMessage(), e);
                }
            } else if (response.getStatus() == 409) {
                log.error("Email already exists: {}", signUpRequest.getEmail());
                throw new IllegalAccessException("이미 가입된 이메일입니다");
            } else {
                log.error("Failed to create user. Status: {}, Reason: {}",
                        response.getStatus(), response.getStatusInfo().getReasonPhrase());
                throw new IllegalAccessException("사용자 생성에 실패했습니다: " + response.getStatusInfo().getReasonPhrase());
            }

            // MySQL에 user 생성 후 저장
            User newUser = User.toMakeEntity(signUpRequest.getName(), signUpRequest.getEmail(),
                    signUpRequest.getPassword(), signUpRequest.getAccountName());
            try {
                newUser = userRepository.save(newUser);
            } catch (Exception e) {
                throw new RuntimeException("사용자 정보 저장에 실패했습니다");
            }

            // Controller에서 나머지 사용자 디폴트값 설정
            return newUser;
        } catch (Exception e) {
            log.error("SignUp failed: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) throws IllegalAccessException {
        try {
            // 존재하는 사용자인지 검증
            String username;
            User findUser;
            if (signInRequest.getUsername() != null && !signInRequest.getUsername().isEmpty()) {
                username = signInRequest.getUsername();
                findUser = userRepository.getUserByAccountName(username);
                if (findUser == null) {
                    throw new IllegalAccessException("NotFoundUsername");
                }
            } else if (signInRequest.getEmail() != null && !signInRequest.getEmail().isEmpty()) {
                username = signInRequest.getEmail();
                findUser = userRepository.getUserByEmail(username);
                if (findUser == null) {
                    throw new IllegalAccessException("NotFoundEmail");
                }
            } else {
                throw new IllegalAccessException("please enter username or email");
            }

            // Keycloak에서 토큰 발행
            try {
                Keycloak keycloak = KeycloakBuilder.builder()
                        .realm(realm)
                        .serverUrl(getKeycloakServerUrl())
                        .grantType("password")
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .username(username)
                        .password(signInRequest.getPassword())
                        .build();

                AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
                log.info("Token response received\nAccess Token: {}\nRefresh Token: {}",
                        tokenResponse.getToken(), tokenResponse.getRefreshToken());
                return new SignInResponse(tokenResponse.getToken(), tokenResponse.getRefreshToken(),
                        findUser.getUserId());
            } catch (Exception e) {
                // Keycloak 인증 실패 (비밀번호 불일치 등)
                log.error("Keycloak authentication error: {}", e.getMessage());

                // HTTP 401 Unauthorized 오류는 일반적으로 인증 실패를 의미
                if (e.getMessage().contains("401") ||
                        e.getMessage().contains("Unauthorized") ||
                        e.getMessage().contains("Invalid user credentials") ||
                        e.getMessage().contains("invalid_grant")) {
                    throw new IllegalAccessException("NotMatchingPassword");
                }
                throw e;
            }
        } catch (Exception e) {
            log.error("SignIn failed: {}", e.getMessage());
            throw e;
        }
    }

    public AccessTokenResponse refreshToken(String refreshToken) {
        try {
            String tokenEndpoint = baseUrl + "/protocol/openid-connect/token";
            String requestBody = String.format(
                    "grant_type=refresh_token&client_id=%s&client_secret=%s&refresh_token=%s",
                    clientId, clientSecret, refreshToken);

            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

            org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(requestBody,
                    headers);
            org.springframework.http.ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(
                    tokenEndpoint, request, AccessTokenResponse.class);

            if (response.getStatusCode() == org.springframework.http.HttpStatus.OK) {
                AccessTokenResponse tokenResponse = response.getBody();
                log.info("Token refreshed successfully\nNew Access Token: {}",
                        tokenResponse.getToken());
                return tokenResponse;
            } else {
                throw new RuntimeException("Token refresh failed with status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            if (e.getMessage().contains("expired") || e.getMessage().contains("invalid_token")) {
                throw new RuntimeException("만료된 토큰입니다. 다시 로그인해주세요.");
            }
            throw new RuntimeException("토큰 갱신에 실패했습니다: " + e.getMessage());
        }
    }
}
