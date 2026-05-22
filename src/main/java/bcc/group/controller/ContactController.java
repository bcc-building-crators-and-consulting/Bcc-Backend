package bcc.group.controller;


import bcc.group.dto.contact.ContactRequest;
import bcc.group.entity.ContactMessage;
import bcc.group.repository.ContactMessageRepository;
import bcc.group.services.impl.ContactMessageService;
import bcc.group.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageService service;
    private  final ContactMessageRepository repository;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> submitContact(@Valid @RequestBody ContactRequest request) {
        service.saveMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Thank you! Your message has been received. We'll get back to you soon.", null));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ContactMessage>>> getAllMessages() {
        List<ContactMessage> messages = repository.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Messages fetched", messages));
    }
}
