package bcc.group.services.impl;

import bcc.group.dto.contact.ContactRequest;
import bcc.group.entity.ContactMessage;
import bcc.group.mapper.ContactMapper;
import bcc.group.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactMessageService {

    private final ContactMessageRepository repository;
    private final ContactMapper mapper;

    // Email fields – injected by Spring (optional, will work only if mail starter is present)
    private final JavaMailSender mailSender;

    @Value("${app.contact.notification-email}")
    private String notificationEmail;

    @Transactional
    public void saveMessage(ContactRequest request) {
        // 1. Save to database
        ContactMessage message = mapper.toEntity(request);
        repository.save(message);
        log.info("Contact message saved from: {}", request.email());

        // 2. Send email notification
        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(notificationEmail);
            mail.setSubject("New Contact Enquiry from " + request.fullName());
            mail.setText(
                    "Name: "    + request.fullName() + "\n" +
                            "Email: "   + request.email()    + "\n" +
                            "Phone: "   + request.phone()    + "\n" +
                            "Service: " + request.service()  + "\n" +
                            "Message: " + request.message()
            );
            mailSender.send(mail);
            log.info("Notification email sent to {}", notificationEmail);
        } catch (Exception e) {
            log.error("Failed to send notification email: {}", e.getMessage());
            // Don't rethrow – the message is already saved
        }
    }
}