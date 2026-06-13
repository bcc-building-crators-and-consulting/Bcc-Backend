package bcc.group.mapper;


import bcc.group.dto.blog.BlogResponse;
import bcc.group.dto.blog.CreateBlogRequest;
import bcc.group.entity.Blog;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    BlogResponse toDto(Blog blog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Blog toEntity(CreateBlogRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CreateBlogRequest request, @MappingTarget Blog blog);
}