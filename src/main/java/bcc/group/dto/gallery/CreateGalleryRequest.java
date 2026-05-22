package bcc.group.dto.gallery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record CreateGalleryRequest(
        @NotBlank @Size(max = 200) String title,
        String category,
        String description,
        String location,
        LocalDate date,
        List<String> tags,
        Integer displayOrder,
        Boolean isActive
) {}
