package bcc.group.entity;

import bcc.group.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "gallery")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class Gallery extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;          // caption / short title

    @Column(name = "image_url")
    private String imageUrl;       // file path

    @Column(length = 100)
    private String category;       // "Project", "Event", "Office", "Site", etc.

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 150)
    private String location;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "gallery_tags", joinColumns = @JoinColumn(name = "gallery_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "display_order")
    private Integer displayOrder;  // sorting

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;
}
