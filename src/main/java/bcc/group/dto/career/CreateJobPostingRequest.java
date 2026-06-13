package bcc.group.dto.career;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateJobPostingRequest(
        @NotBlank String title,
        @NotBlank String department,
        @NotBlank String location,
        String jobType,
        @NotBlank String description,
        String requirements,
        String responsibilities,
        String experienceRequired,
        String salaryRange,
        Integer noOfOpenings,
        Boolean isActive,
        LocalDate postedDate,
        LocalDate lastDateToApply
) {}