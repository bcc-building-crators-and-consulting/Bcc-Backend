package bcc.group.services;


import bcc.group.dto.blog.BlogResponse;
import bcc.group.dto.blog.CreateBlogRequest;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public interface BlogService {
    // existing methods (unchanged)
    List<BlogResponse> getAllBlogs();
    BlogResponse getBlogById(Long id);
    BlogResponse getBlogBySlug(String slug);
    List<BlogResponse> getBlogsByCategory(String category);
    void deleteBlog(Long id);

    // NEW: create/update with file upload
    BlogResponse createBlog(CreateBlogRequest request, MultipartFile coverImage);
    BlogResponse updateBlog(Long id, CreateBlogRequest request, MultipartFile coverImage);
    
}
