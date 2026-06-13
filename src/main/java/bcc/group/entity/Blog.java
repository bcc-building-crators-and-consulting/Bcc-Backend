package bcc.group.entity;

import bcc.group.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "blogs")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder        // ← now extends builder from BaseEntity
public class Blog extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 250)
    private String slug;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String excerpt;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(length = 100)
    private String author;

    @Column(length = 50)
    private String category;

    @ElementCollection
    @CollectionTable(name = "blog_tags", joinColumns = @JoinColumn(name = "blog_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "read_time_minutes")
    private Integer readTimeMinutes;

    @Column(name = "is_published")
    @Builder.Default
    private Boolean isPublished = false;

    @Column(name = "published_date")
    private LocalDate publishedDate;
}