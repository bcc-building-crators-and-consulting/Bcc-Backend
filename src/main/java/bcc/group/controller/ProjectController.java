package bcc.group.controller;

import bcc.group.dto.project.CreateProjectRequest;
import bcc.group.dto.project.ProjectResponse;
import bcc.group.services.ProjectService;
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
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")   // adjust to your frontend URL(s)
@Tag(name = "Projects", description = "Project management APIs")
public class ProjectController {

    private final ProjectService projectService;

    // ==================== CREATE (with multipart) ====================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestPart("project") CreateProjectRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
            @RequestPart(value = "images", required = false) List<MultipartFile> additionalImages) {
        ProjectResponse created = projectService.createProject(request, coverImage, additionalImages);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Project created successfully", created));
    }

    // ==================== UPDATE (with multipart) ====================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestPart("project") CreateProjectRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
            @RequestPart(value = "images", required = false) List<MultipartFile> additionalImages) {
        ProjectResponse updated = projectService.updateProject(id, request, coverImage, additionalImages);
        return ResponseEntity.ok(new ApiResponse<>(true, "Project updated successfully", updated));
    }

    // ==================== READ – ALL ====================
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Projects fetched successfully", projects));
    }

    // ==================== READ – BY ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        ProjectResponse project = projectService.getProjectById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Project fetched successfully", project));
    }

    // ==================== READ – BY STATUS ====================
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectsByStatus(@PathVariable String status) {
        List<ProjectResponse> projects = projectService.getProjectsByStatus(status);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Projects fetched by status", projects));
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Project deleted successfully", null));
    }
}