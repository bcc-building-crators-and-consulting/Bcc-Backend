package bcc.group.mapper.careers;


import bcc.group.dto.career.CreateJobPostingRequest;
import bcc.group.dto.career.JobPostingResponse;
import bcc.group.entity.career.JobPosting;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JobPostingMapper {

    JobPostingResponse toDto(JobPosting entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    JobPosting toEntity(CreateJobPostingRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CreateJobPostingRequest request, @MappingTarget JobPosting entity);
}