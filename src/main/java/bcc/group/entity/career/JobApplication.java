package bcc.group.entity.career;

import bcc.group.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class JobApplication extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "position")
    private String position;          // job title applied for

    @Column(name = "experience")
    private String experience;        // years of experience as string

    @Column(name = "current_company")
    private String currentCompany;

    @Column(name = "portfolio")
    private String portfolio;         // portfolio or LinkedIn URL

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "resume_file_path")
    private String resumeFilePath;

    // Optional relationship – may be null for open applications
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "job_posting_id", nullable = true)
    private JobPosting jobPosting;
}