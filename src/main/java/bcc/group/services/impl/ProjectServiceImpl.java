package bcc.group.services.impl;

import bcc.group.dto.project.CreateProjectRequest;
import bcc.group.dto.project.ProjectResponse;
import bcc.group.entity.Project;
import bcc.group.exception.ResourceNotFoundException;
import bcc.group.mapper.ProjectMapper;
import bcc.group.repository.ProjectRepository;
import bcc.group.services.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Value("${app.upload.project-dir:uploads/images/projects}")
    private String projectImageDir;

    // ---------- Existing methods (unchanged) ----------
    @Override
    @Cacheable("projects")
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status)
                .stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.deleteById(id);
    }

    // ---------- NEW: File upload versions ----------

    @Override
    @CacheEvict(value = "projects", allEntries = true)
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request,
                                         MultipartFile coverImage,
                                         List<MultipartFile> additionalImages) {
        log.debug("Creating project (with files): {}", request.title());
        Project entity = projectMapper.toEntity(request);

        // Save cover image
        if (coverImage != null && !coverImage.isEmpty()) {
            entity.setCoverImageUrl(saveImage(coverImage));
        }

        // Save additional images
        if (additionalImages != null && !additionalImages.isEmpty()) {
            List<String> paths = additionalImages.stream()
                    .filter(f -> !f.isEmpty())
                    .map(this::saveImage)
                    .collect(Collectors.toList());
            entity.setImageUrls(paths);
        }

        Project saved = projectRepository.save(entity);
        return projectMapper.toDto(saved);
    }

    @Transactional
    @Override
    public ProjectResponse updateProject(Long id,
                                         CreateProjectRequest request,
                                         MultipartFile coverImage,
                                         List<MultipartFile> additionalImages) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        projectMapper.updateEntityFromRequest(request, project);

        if (coverImage != null && !coverImage.isEmpty()) {
            project.setCoverImageUrl(saveImage(coverImage));
        }

        if (additionalImages != null && !additionalImages.isEmpty()) {
            List<String> paths = additionalImages.stream()
                    .filter(f -> !f.isEmpty())
                    .map(this::saveImage)
                    .collect(Collectors.toList());
            project.setImageUrls(paths);
        }

        return projectMapper.toDto(projectRepository.save(project));
    }

    // ---------- Private helper ----------
    private String saveImage(MultipartFile file) {
        try {
            // ✅ Use projectImageDir directly (already points to .../uploads/images/projects)
            Path uploadPath = Paths.get(projectImageDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uniqueName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(uniqueName);
            file.transferTo(filePath.toFile());

            // ✅ URL matches ResourceHandler mapping
            return "/uploads/images/projects/" + uniqueName;

        } catch (IOException e) {
            log.error("Failed to store projects image", e);
            throw new RuntimeException("Could not store projects image file");
        }
    }
}