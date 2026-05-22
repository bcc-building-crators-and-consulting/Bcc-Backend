package bcc.group.services.impl;

import bcc.group.dto.team.CreateTeamMemberRequest;
import bcc.group.dto.team.TeamMemberResponse;
import bcc.group.entity.TeamMember;
import bcc.group.exception.ResourceNotFoundException;
import bcc.group.mapper.TeamMemberMapper;
import bcc.group.repository.TeamMemberRepository;
import bcc.group.services.TeamMemberService;
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
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository repository;
    private final TeamMemberMapper mapper;

    @Value("${app.upload.team-dir:uploads/images/members}")
    private String teamUploadDir;
    // ──────────────────────────────────────────────
    // NEW: Create a team member WITHOUT an image (JSON only)
    // ──────────────────────────────────────────────
    @Override
    @CacheEvict(value = "teamMembers", allEntries = true)
    @Transactional
    public TeamMemberResponse create(CreateTeamMemberRequest request) {
        log.debug("Creating team member (JSON only): {}", request.name());
        TeamMember member = mapper.toEntity(request);
        TeamMember saved = repository.save(member);
        return mapper.toDto(saved);
    }

    // ──────────────────────────────────────────────
    // ORIGINAL: Create a team member WITH an image file
    // ──────────────────────────────────────────────
    @Override
    @CacheEvict(value = "teamMembers", allEntries = true)
    @Transactional
    public TeamMemberResponse create(CreateTeamMemberRequest request, MultipartFile imageFile) {
        log.debug("Creating team member with image: {}", request.name());
        TeamMember member = mapper.toEntity(request);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImageFile(imageFile);
            member.setProfileImageUrl(imagePath);
        }

        TeamMember saved = repository.save(member);
        return mapper.toDto(saved);
    }

    // ──────────────────────────────────────────────
    // NEW: Upload/update image for an existing member
    // ──────────────────────────────────────────────
    @Override
    @Transactional
    public TeamMemberResponse updateImage(Long id, MultipartFile imageFile) {
        TeamMember member = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImageFile(imageFile);
            member.setProfileImageUrl(imagePath);
            repository.save(member);
        }
        return mapper.toDto(member);
    }

    // ──────────────────────────────────────────────
    // REST OF THE METHODS (unchanged)
    // ──────────────────────────────────────────────

    @Override
    @Cacheable("teamMembers")
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getActiveMembers() {
        return repository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMemberResponse getById(Long id) {
        TeamMember member = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));
        return mapper.toDto(member);
    }

    @Override
    @CacheEvict(value = "teamMembers", allEntries = true)
    @Transactional
    public TeamMemberResponse update(Long id, CreateTeamMemberRequest request, MultipartFile imageFile) {
        TeamMember member = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));

        mapper.updateEntityFromRequest(request, member);

        if (imageFile != null && !imageFile.isEmpty()) {
            String newImagePath = saveImageFile(imageFile);
            member.setProfileImageUrl(newImagePath);
        }

        return mapper.toDto(repository.save(member));
    }

    @Override
    @Transactional
    public TeamMemberResponse update(Long id, CreateTeamMemberRequest request) {
        TeamMember member = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));

        mapper.updateEntityFromRequest(request, member);
        return mapper.toDto(repository.save(member));
    }

    @Override
    @CacheEvict(value = "teamMembers", allEntries = true)
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Team member not found with id: " + id);
        repository.deleteById(id);
    }

    private String saveImageFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(teamUploadDir);
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
            return "/uploads/images/members/" + uniqueName;
        } catch (IOException e) {
            log.error("Failed to save profile image", e);
            throw new RuntimeException("Could not store profile image file");
        }
    }
}
