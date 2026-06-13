package bcc.group.services.careersService;


import bcc.group.dto.career.CreateJobPostingRequest;
import bcc.group.dto.career.JobPostingResponse;

import java.util.List;

public interface JobPostingService {
    JobPostingResponse create(CreateJobPostingRequest request);
    List<JobPostingResponse> getAll();
    List<JobPostingResponse> getActive();
    JobPostingResponse getById(Long id);
    JobPostingResponse update(Long id, CreateJobPostingRequest request);
    void delete(Long id);
}