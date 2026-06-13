package bcc.group.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
// Remove imageUrls field entirely (files replace them)
public record CreateProjectRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank String description,
        String clientName,
        String location,
        String projectType,
        String status,
        LocalDate startDate,
        LocalDate completionDate,
        String testimonial
) {}