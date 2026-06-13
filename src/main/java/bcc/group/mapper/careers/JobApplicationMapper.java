package bcc.group.mapper.careers;

import bcc.group.dto.career.CreateJobApplicationRequest;
import bcc.group.dto.career.JobApplicationResponse;
import bcc.group.entity.career.JobApplication;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    // Entity -> Response (fields match by name)
    JobApplicationResponse toDto(JobApplication entity);

    // Request -> Entity (ignore auto-generated fields)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "resumeFilePath", ignore = true) // set after upload
    JobApplication toEntity(CreateJobApplicationRequest request);
}