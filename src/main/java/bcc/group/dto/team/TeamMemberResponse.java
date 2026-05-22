package bcc.group.dto.team;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TeamMemberResponse(
        Long id,
        String name,
        String designation,
        String department,
        String profileImageUrl,   //
        String bio,
        String email,
        String phone,
        String linkedinUrl,
        List<String> qualifications,
        Integer yearsOfExperience,
        Integer displayOrder,
        Boolean isActive,
        LocalDate joinedDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
