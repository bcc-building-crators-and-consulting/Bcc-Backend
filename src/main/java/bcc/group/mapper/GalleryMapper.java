package bcc.group.mapper;


import bcc.group.dto.gallery.CreateGalleryRequest;
import bcc.group.dto.gallery.GalleryResponse;
import bcc.group.entity.Gallery;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GalleryMapper {

    GalleryResponse toDto(Gallery entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Gallery toEntity(CreateGalleryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    void updateEntityFromRequest(CreateGalleryRequest request, @MappingTarget Gallery entity);
}
