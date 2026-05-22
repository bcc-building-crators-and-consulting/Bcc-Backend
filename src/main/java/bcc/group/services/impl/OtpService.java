package bcc.group.services.impl;

import bcc.group.entity.OtpCode;
import bcc.group.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    // Generate & send OTP
    @Transactional
    public void sendOtp(String email) {
        // Delete old OTP for this email
        otpRepository.deleteByEmail(email);

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save OTP with 5-minute expiry
        OtpCode otpCode = OtpCode.builder()
                .email(email)
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .createdAt(LocalDateTime.now())
                .build();
        otpRepository.save(otpCode);

        // Send OTP via email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Your OTP Code - BCC Consulting");
        mail.setText("Your OTP code is: " + otp + "\n\nThis code expires in 5 minutes.");
        mailSender.send(mail);

        log.info("OTP sent to {}", email);
    }

    // Verify OTP
    public boolean verifyOtp(String email, String otp) {
        return otpRepository.findByEmailAndOtpAndVerifiedFalse(email, otp)
                .filter(otpCode -> otpCode.getExpiryTime().isAfter(LocalDateTime.now()))
                .map(otpCode -> {
                    otpCode.setVerified(true);
                    otpRepository.save(otpCode);
                    return true;
                })
                .orElse(false);
    }
}
