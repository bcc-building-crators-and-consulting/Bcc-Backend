package bcc.group.services;


import bcc.group.dto.gallery.CreateGalleryRequest;
import bcc.group.dto.gallery.GalleryResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface GalleryService {
    GalleryResponse create(CreateGalleryRequest request, MultipartFile imageFile);
    List<GalleryResponse> getAll();
    List<GalleryResponse> getByCategory(String category);
    GalleryResponse getById(Long id);
    GalleryResponse update(Long id, CreateGalleryRequest request, MultipartFile imageFile);
    void delete(Long id);
}
