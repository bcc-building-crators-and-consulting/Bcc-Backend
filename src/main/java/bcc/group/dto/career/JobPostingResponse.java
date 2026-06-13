package bcc.group.dto.career;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record JobPostingResponse(
        Long id,
        String title,
        String department,
        String location,
        String jobType,
        String description,
        String requirements,
        String responsibilities,
        String experienceRequired,
        String salaryRange,
        Integer noOfOpenings,
        Boolean isActive,
        LocalDate postedDate,
        LocalDate lastDateToApply,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}