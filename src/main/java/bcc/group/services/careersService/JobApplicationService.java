package bcc.group.services.careersService;


import bcc.group.dto.career.CreateJobApplicationRequest;
import bcc.group.dto.career.JobApplicationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobApplicationService {
    JobApplicationResponse apply(CreateJobApplicationRequest request, MultipartFile resumeFile);
    List<JobApplicationResponse> getApplicationsByJobPosting(Long jobPostingId);
    List<JobApplicationResponse> getAllApplications();
    JobApplicationResponse getById(Long id);
    void delete(Long id);
}