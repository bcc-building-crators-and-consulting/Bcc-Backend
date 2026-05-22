package bcc.group.services.impl;


import bcc.group.dto.gallery.CreateGalleryRequest;
import bcc.group.dto.gallery.GalleryResponse;
import bcc.group.entity.Gallery;
import bcc.group.exception.ResourceNotFoundException;
import bcc.group.mapper.GalleryMapper;
import bcc.group.repository.GalleryRepository;
import bcc.group.services.GalleryService;
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
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository repository;
    private final GalleryMapper mapper;

    @Value("${app.upload.dir}")
    private String galleryDir;

    @Override
    @CacheEvict(value = "gallery", allEntries = true)
    @Transactional
    public GalleryResponse create(CreateGalleryRequest request, MultipartFile imageFile) {
        log.debug("Creating gallery item: {}", request.title());
        Gallery gallery = mapper.toEntity(request);
        if (imageFile != null && !imageFile.isEmpty()) {
            gallery.setImageUrl(saveImage(imageFile));
        }
        return mapper.toDto(repository.save(gallery));
    }

    @Override
    @Cacheable("gallery")
    @Transactional(readOnly = true)
    public List<GalleryResponse> getAll() {
        return repository.findAllByOrderByDisplayOrderAsc()
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<GalleryResponse> getByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public GalleryResponse getById(Long id) {
        Gallery gallery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery item not found"));
        return mapper.toDto(gallery);
    }

    @Override @Transactional
    public GalleryResponse update(Long id, CreateGalleryRequest request, MultipartFile imageFile) {
        Gallery gallery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery item not found"));
        mapper.updateEntityFromRequest(request, gallery);
        if (imageFile != null && !imageFile.isEmpty()) {
            gallery.setImageUrl(saveImage(imageFile));
        }
        return mapper.toDto(repository.save(gallery));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Gallery item not found");
        repository.deleteById(id);
    }

    private String saveImage(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(galleryDir, "images/gallery");
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(uniqueName);
            file.transferTo(filePath.toFile());
            return "/uploads/images/gallery/" + uniqueName;
        } catch (IOException e) {
            log.error("Failed to store gallery image", e);
            throw new RuntimeException("Could not store gallery image file");
        }
    }
}
