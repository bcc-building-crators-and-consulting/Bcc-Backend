package bcc.group.dto.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record CreateBlogRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 250) String slug,
        @NotBlank String content,
        String excerpt,
        // String coverImageUrl,   ← REMOVED
        String author,
        String category,
        List<String> tags,
        Integer readTimeMinutes,
        Boolean isPublished,
        LocalDate publishedDate
) {}