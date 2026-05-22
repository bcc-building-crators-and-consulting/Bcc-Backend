package bcc.group.controller;

import bcc.group.dto.gallery.CreateGalleryRequest;
import bcc.group.dto.gallery.GalleryResponse;
import bcc.group.services.GalleryService;
import bcc.group.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
@Tag(name = "Gallery", description = "Gallery management APIs")
public class GalleryController {

    private final GalleryService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<GalleryResponse>> create(
            @Valid @RequestPart("gallery") CreateGalleryRequest request,
            @RequestPart("image") MultipartFile imageFile) {
        GalleryResponse created = service.create(request, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Gallery item created", created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<GalleryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestPart("gallery") CreateGalleryRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        GalleryResponse updated = service.update(id, request, imageFile);
        return ResponseEntity.ok(new ApiResponse<>(true, "Gallery item updated", updated));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GalleryResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Gallery items", service.getAll()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<GalleryResponse>>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Gallery items by category", service.getByCategory(category)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GalleryResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Gallery item found", service.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Gallery item deleted", null));
    }
}