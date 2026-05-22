package bcc.group.entity;

import bcc.group.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder        // ⬅️ Now builder includes id, createdAt, updatedAt
public class Project extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "client_name")
    private String clientName;

    private String location;

    @Column(name = "project_type")
    private String projectType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    private String status;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @ElementCollection
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Column(columnDefinition = "TEXT")
    private String testimonial;
}