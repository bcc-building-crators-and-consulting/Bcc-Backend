package bcc.group.services.impl;

import bcc.group.dto.blog.BlogResponse;
import bcc.group.dto.blog.CreateBlogRequest;
import bcc.group.entity.Blog;
import bcc.group.exception.ResourceNotFoundException;
import bcc.group.mapper.BlogMapper;
import bcc.group.repository.BlogRepository;
import bcc.group.services.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Value("${app.upload.blog-dir:uploads/images/blogs}")
    private String blogImageDir;

    // ─────────────────────────────────────────────
    // Existing CRUD methods (fully implemented)
    // ─────────────────────────────────────────────
    @Override
    @Cacheable("blogs")
    @Transactional(readOnly = true)
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(blogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        return blogMapper.toDto(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogResponse getBlogBySlug(String slug) {
        Blog blog = blogRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with slug: " + slug));
        return blogMapper.toDto(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogResponse> getBlogsByCategory(String category) {
        return blogRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(blogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        blogRepository.deleteById(id);
    }

    // ─────────────────────────────────────────────
    // NEW: Create / Update with file upload
    // ─────────────────────────────────────────────
    @Override
    @CacheEvict(value = "blogs", allEntries = true)
    @Transactional
    public BlogResponse createBlog(CreateBlogRequest request, MultipartFile coverImage) {
        log.debug("Creating blog with file: {}", request.slug());
        Blog blog = blogMapper.toEntity(request);
        if (coverImage != null && !coverImage.isEmpty()) {
            blog.setCoverImageUrl(saveImage(coverImage));
        }
        Blog saved = blogRepository.save(blog);
        return blogMapper.toDto(saved);
    }

    @Override
    @CacheEvict(value = "blogs", allEntries = true)
    @Transactional
    public BlogResponse updateBlog(Long id, CreateBlogRequest request, MultipartFile coverImage) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        blogMapper.updateEntityFromRequest(request, blog);
        if (coverImage != null && !coverImage.isEmpty()) {
            blog.setCoverImageUrl(saveImage(coverImage));
        }
        return blogMapper.toDto(blogRepository.save(blog));
    }

    // ─────────────────────────────────────────────
    // Private helper (relative URL)
    // ─────────────────────────────────────────────
    private String saveImage(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(blogImageDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(uniqueName);
            file.transferTo(filePath.toFile());
            return "/uploads/images/blogs/" + uniqueName;   // ← relative URL for serving
        } catch (IOException e) {
            log.error("Failed to store blog cover image", e);
            throw new RuntimeException("Could not store cover image file");
        }
    }
}