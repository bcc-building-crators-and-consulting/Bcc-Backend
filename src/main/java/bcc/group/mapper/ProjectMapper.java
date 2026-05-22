package bcc.group.mapper;


import bcc.group.dto.project.CreateProjectRequest;
import bcc.group.dto.project.ProjectResponse;
import bcc.group.entity.Project;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    // Entity → Response DTO
    ProjectResponse toDto(Project project);

    // Request DTO → Entity (for create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project toEntity(CreateProjectRequest request);

    // Request DTO → update existing entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CreateProjectRequest request, @MappingTarget Project project);
}