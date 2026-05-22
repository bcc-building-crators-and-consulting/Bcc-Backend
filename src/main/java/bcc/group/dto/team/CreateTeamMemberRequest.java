package bcc.group.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record CreateTeamMemberRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 100) String designation,
        String department,
        String bio,
        String email,
        String phone,
        String linkedinUrl,
        List<String> qualifications,
        Integer yearsOfExperience,
        Integer displayOrder,
        Boolean isActive,
        LocalDate joinedDate
) {}
