package bcc.group.controller;


import bcc.group.dto.blog.BlogResponse;
import bcc.group.dto.blog.CreateBlogRequest;
import bcc.group.services.BlogService;
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
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Blog management APIs")
public class BlogController {

    private final BlogService blogService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(
            @Valid @RequestPart("blog") CreateBlogRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        BlogResponse created = blogService.createBlog(request, coverImage);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Blog created", created));
    }



    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogResponse>>> getAllBlogs() {
        List<BlogResponse> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(new ApiResponse<>(true, "Blogs fetched successfully", blogs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogById(@PathVariable Long id) {
        BlogResponse blog = blogService.getBlogById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Blog fetched successfully", blog));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogBySlug(@PathVariable String slug) {
        BlogResponse blog = blogService.getBlogBySlug(slug);
        return ResponseEntity.ok(new ApiResponse<>(true, "Blog fetched successfully", blog));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<BlogResponse>>> getBlogsByCategory(@PathVariable String category) {
        List<BlogResponse> blogs = blogService.getBlogsByCategory(category);
        return ResponseEntity.ok(new ApiResponse<>(true, "Blogs by category", blogs));
    }

    // UPDATE with file
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(
            @PathVariable Long id,
            @Valid @RequestPart("blog") CreateBlogRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        BlogResponse updated = blogService.updateBlog(id, request, coverImage);
        return ResponseEntity.ok(new ApiResponse<>(true, "Blog updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Blog deleted successfully", null));
    }
}