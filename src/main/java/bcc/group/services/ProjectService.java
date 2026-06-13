package bcc.group.services;

import bcc.group.dto.project.CreateProjectRequest;
import bcc.group.dto.project.ProjectResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(Long id);
    List<ProjectResponse> getProjectsByStatus(String status);
    void deleteProject(Long id);
    // ---------- NEW: File upload versions ----------
    @Transactional
    ProjectResponse createProject(CreateProjectRequest request,
                                  MultipartFile coverImage,
                                  List<MultipartFile> additionalImages);

    @Transactional
    ProjectResponse updateProject(Long id,
                                  CreateProjectRequest request,
                                  MultipartFile coverImage,
                                  List<MultipartFile> additionalImages);
}