package bcc.group.dto.career;

import java.time.LocalDateTime;

public record JobApplicationResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        Long jobPostingId,
        String jobTitle,                // convenience field
        String yearsOfExperience,
        String currentCompany,
        String portfolioUrl,
        String resumeFilePath,
        String coverMessage,
        LocalDateTime createdAt
) {}