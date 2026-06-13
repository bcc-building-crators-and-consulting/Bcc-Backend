package bcc.group.dto.gallery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record GalleryResponse(
        Long id,
        String title,
        String imageUrl,
        String category,
        String description,
        String location,
        LocalDate date,
        List<String> tags,
        Integer displayOrder,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
