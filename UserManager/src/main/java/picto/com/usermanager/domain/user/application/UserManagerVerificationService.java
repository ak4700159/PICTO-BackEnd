package picto.com.usermanager.domain.user.application;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import picto.com.usermanager.domain.user.dao.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.Random;
import picto.com.usermanager.domain.user.entity.EmailVerification;
import picto.com.usermanager.domain.user.dto.response.EmailVerificationResponse;
import org.keycloak.representations.idm.CredentialRepresentation;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagerVerificationService {

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

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private JavaMailSender mailSender;

    private String getKeycloakServerUrl() {
        return baseUrl.substring(0, baseUrl.indexOf("/realms/"));
    }

    @Transactional
    public void verifyDuplicatedUser(String userEmail) throws IllegalAccessException {
        if (userRepository.getUserByEmail(userEmail) != null) {
            System.out.println("중복된 유저");
            throw new IllegalAccessException("Duplicated");
        }
    }

    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return code.toString();
    }

    @Transactional
    public void sendVerifyEmail(String email) {
        // 이미 전송된 인증 이메일이 있으면 삭제
        // 이메일을 여러번 전송해도 제일 최근 인증 이메일만 사용하도록
        List<EmailVerification> verification = emailVerificationRepository.findByEmail(email);
        if (verification != null) {
            emailVerificationRepository.deleteAll(verification);
        }

        // 인증 코드 생성 (알파벳 대문자, 숫자 섞어서 6자리)
        String code = generateRandomCode();
        long now = Instant.now().toEpochMilli();
        EmailVerification entity = EmailVerification.toMakeEntity(email, code, now);
        emailVerificationRepository.save(entity);

        // 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("PICTO ADMIN <" + this.email + ">");
        message.setSubject("Picto 어플리케이션 이메일 인증 코드입니다.");
        message.setText("아래 인증 코드를 10분 이내에 입력해주세요.\n" + code);
        mailSender.send(message);
    }

    @Transactional
    public EmailVerificationResponse verifyEmail(String email, String code) {
        EmailVerification verification = emailVerificationRepository.findByEmailAndCode(email, code);

        // 코드 검증
        if (verification == null) {
            return new EmailVerificationResponse("인증 코드가 일치하지 않습니다.", false);
        }

        // 10분 시간제한
        long now = Instant.now().toEpochMilli();
        if (now - verification.getRequestDatetime() > 600_000) {
            return new EmailVerificationResponse("인증 코드가 만료되었습니다.", false);
        }

        // 인증 성공 시 인증 정보 삭제
        emailVerificationRepository.delete(verification);

        return new EmailVerificationResponse("이메일 인증에 성공했습니다.", true);
    }

    @Transactional
    public EmailVerificationResponse isVerifiedEmail(String email) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(getKeycloakServerUrl())
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();

            List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, 0, 1);
            if (!users.isEmpty()) {
                UserRepresentation user = users.get(0);
                if (user.isEmailVerified()) {
                    return new EmailVerificationResponse("인증된 사용자입니다.", true);
                } else {
                    return new EmailVerificationResponse("인증되지 않은 사용자입니다.", false);
                }
            }
            return new EmailVerificationResponse("존재하지 않는 이메일입니다.", false);
        } catch (Exception e) {
            log.error("Keycloak 이메일 인증 상태 확인 실패: {}", e.getMessage());
            return new EmailVerificationResponse("이메일 인증 상태 확인 실패", false);
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            code.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return code.toString();
    }

    @Transactional
    public void sendTemporaryPassword(String email) {
        // 인증 코드 생성 (알파벳 대문자, 숫자 섞어서 10자리)
        String password = generateRandomPassword();

        try {
            // Keycloak에서 사용자 검색 및 비밀번호 업데이트
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(getKeycloakServerUrl())
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();

            List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, 0, 1);
            if (!users.isEmpty()) {
                UserRepresentation user = users.get(0);
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(password);
                credential.setTemporary(false);

                keycloak.realm(realm).users().get(user.getId()).resetPassword(credential);
            }

            // 이메일 전송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom("PICTO ADMIN <" + this.email + ">");
            message.setSubject("Picto 어플리케이션 임시 비밀번호입니다.");
            message.setText("로그인 후 비밀번호를 변경해주세요.\n" + password);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("비밀번호 재설정 실패: {}", e.getMessage());
            throw new RuntimeException("비밀번호 재설정에 실패했습니다.");
        }
    }
}
