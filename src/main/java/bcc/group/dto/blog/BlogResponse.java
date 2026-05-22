package bcc.group.dto.blog;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BlogResponse(
        Long id,
        String title,
        String slug,
        String content,
        String excerpt,
        String coverImageUrl,
        String author,
        String category,
        List<String> tags,
        Integer readTimeMinutes,
        Boolean isPublished,
        LocalDate publishedDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
