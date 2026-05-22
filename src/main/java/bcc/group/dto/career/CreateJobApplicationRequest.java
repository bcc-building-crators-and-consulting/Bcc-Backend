package bcc.group.dto.career;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateJobApplicationRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String phone,
        String position,               // position is optional if not linking to job posting
        String experience,
        String currentCompany,
        String portfolio,              // matches frontend field
        String coverLetter             // matches frontend field
) {}