package bcc.group.entity;

import bcc.group.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "team_members")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class TeamMember extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String designation;

    @Column(length = 100)
    private String department;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @ElementCollection
    @CollectionTable(name = "member_qualifications", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "qualification")
    private List<String> qualifications;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "display_order")
    private Integer displayOrder;       // for sorting on frontend

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "joined_date")
    private LocalDate joinedDate;
}