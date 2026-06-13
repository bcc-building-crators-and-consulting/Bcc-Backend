package bcc.group.entity.career;

import bcc.group.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@Entity
@Table(name = "job_postings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class JobPosting extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String location;

    @Column(name = "job_type") // Full-time, Part-time, Contract
    private String jobType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(name = "experience_required")
    private String experienceRequired; // e.g., "2-5 years"

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "no_of_openings")
    private Integer noOfOpenings;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    @Column(name = "last_date_to_apply")
    private LocalDate lastDateToApply;
}
