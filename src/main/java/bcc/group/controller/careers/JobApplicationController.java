package bcc.group.controller.careers;


import bcc.group.dto.career.CreateJobApplicationRequest;
import bcc.group.dto.career.JobApplicationResponse;
import bcc.group.services.careersService.JobApplicationService;
import bcc.group.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
@Tag(name = "JobApplication", description = "JobApplication management APIs")
public class JobApplicationController {

    private final JobApplicationService service;

    // Public: Submit application → only thank-you message, no personal data returned
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> apply(
            @Valid @RequestPart("application") CreateJobApplicationRequest request,
            @RequestPart("resume") MultipartFile resumeFile) {
        // Service returns JobApplicationResponse internally, but we discard it here for security
        service.apply(request, resumeFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,
                        "Thank you! Your application has been submitted. Our team will review it and contact you within 48 hours.",
                        null));
    }

    // Admin: View applications by job posting
    @GetMapping("/job-posting/{jobPostingId}")
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getByJobPosting(@PathVariable Long jobPostingId) {
        List<JobApplicationResponse> apps = service.getApplicationsByJobPosting(jobPostingId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Applications for job posting", apps));
    }

    // Admin: View all applications
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getAll() {
        List<JobApplicationResponse> apps = service.getAllApplications();
        return ResponseEntity.ok(new ApiResponse<>(true, "All applications", apps));
    }

    // Admin: View a specific application
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> getById(@PathVariable Long id) {
        JobApplicationResponse app = service.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Application found", app));
    }

    // Admin: Delete an application
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Application deleted", null));
    }
}