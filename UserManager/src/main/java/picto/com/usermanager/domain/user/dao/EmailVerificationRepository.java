package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import picto.com.usermanager.domain.user.entity.EmailVerification;

import java.util.List;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    @Query("select f from EmailVerification f where f.email = :email")
    List<EmailVerification> findByEmail(String email);

    EmailVerification findByVerificationId(Long verificationId);

    @Query("select f from EmailVerification f where f.email = :email and f.code = :code")
    EmailVerification findByEmailAndCode(String email, String code);
}
