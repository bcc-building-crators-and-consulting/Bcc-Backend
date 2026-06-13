package bcc.group.dto.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Use for outgoing data (contains all fields)
public record ProjectResponse(
        Long id,
        String title,
        String description,
        String clientName,
        String location,
        String projectType,
        String status,
        LocalDate startDate,
        LocalDate completionDate,
        String coverImageUrl,
        List<String> imageUrls,
        String testimonial,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}