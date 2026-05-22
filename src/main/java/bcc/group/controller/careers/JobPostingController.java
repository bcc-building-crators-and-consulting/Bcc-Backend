package bcc.group.controller.careers;


import bcc.group.dto.career.CreateJobPostingRequest;
import bcc.group.dto.career.JobPostingResponse;
import bcc.group.services.careersService.JobPostingService;
import bcc.group.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
@Tag(name = "JobPosting", description = "JobPosting management APIs")
public class JobPostingController {

    private final JobPostingService service;


    @PostMapping
    public ResponseEntity<ApiResponse<JobPostingResponse>> create(@Valid @RequestBody CreateJobPostingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Job posting created", service.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "All job postings", service.getAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>> getActive() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Active job postings", service.getActive()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Job posting found", service.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> update(
            @PathVariable Long id, @Valid @RequestBody CreateJobPostingRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Job posting updated", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job posting deleted", null));
    }
}
