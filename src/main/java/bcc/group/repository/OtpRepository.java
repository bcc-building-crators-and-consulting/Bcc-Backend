package bcc.group.repository;

import bcc.group.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByEmailAndOtpAndVerifiedFalse(String email, String otp);
    Optional<OtpCode> findTopByEmailOrderByCreatedAtDesc(String email);
    void deleteByEmail(String email);
}