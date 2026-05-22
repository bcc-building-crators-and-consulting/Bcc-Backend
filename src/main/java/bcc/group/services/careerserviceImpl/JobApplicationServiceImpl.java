package bcc.group.services.careerserviceImpl;

import bcc.group.dto.career.CreateJobApplicationRequest;
import bcc.group.dto.career.JobApplicationResponse;
import bcc.group.entity.career.JobApplication;
import bcc.group.exception.ResourceNotFoundException;
import bcc.group.mapper.careers.JobApplicationMapper;
import bcc.group.repository.careers.JobApplicationRepository;
import bcc.group.services.careersService.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobApplicationMapper mapper;
    private final JavaMailSender mailSender;

    @Value("${app.upload.resume-dir}")
    private String resumeDir;

    @Value("${app.contact.notification-email}")
    private String notificationEmail;

    @Override
    @Transactional
    public JobApplicationResponse apply(CreateJobApplicationRequest request, MultipartFile resumeFile) {
        // 1. Map request to entity
        JobApplication application = mapper.toEntity(request);

        // 2. Save resume file
        if (resumeFile != null && !resumeFile.isEmpty()) {
            String filePath = saveResumeFile(resumeFile);
            application.setResumeFilePath(filePath);
        }

        // 3. Save to database
        JobApplication saved = applicationRepository.save(application);
        log.info("Job application saved from: {}", request.email());

        // 4. Send email notification
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(notificationEmail);
            mail.setSubject("New Job Application from " + request.name());
            mail.setText(
                    "New job application received:\n\n" +
                            "Name:            " + request.name()           + "\n" +
                            "Email:           " + request.email()          + "\n" +
                            "Phone:           " + request.phone()          + "\n" +
                            "Position:        " + request.position()       + "\n" +
                            "Experience:      " + request.experience()     + "\n" +
                            "Current Company: " + request.currentCompany() + "\n" +
                            "Portfolio:       " + request.portfolio()      + "\n" +
                            "Cover Letter:    " + request.coverLetter()    + "\n" +
                            "Resume:          " + (saved.getResumeFilePath() != null
                            ? saved.getResumeFilePath()
                            : "Not uploaded")
            );
            mailSender.send(mail);
            log.info("Notification email sent to {}", notificationEmail);
        } catch (Exception e) {
            log.error("Failed to send notification email: {}", e.getMessage());
            // Don't rethrow – application is already saved
        }

        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getApplicationsByJobPosting(Long jobPostingId) {
        return applicationRepository.findAll().stream()
                .filter(app -> app.getJobPosting() != null &&
                        app.getJobPosting().getId().equals(jobPostingId))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getById(Long id) {
        JobApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        return mapper.toDto(app);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!applicationRepository.existsById(id))
            throw new ResourceNotFoundException("Application not found");
        applicationRepository.deleteById(id);
    }

    private String saveResumeFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(resumeDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(uniqueName);
            file.transferTo(filePath.toFile());
            return "/uploads/resumes/" + uniqueName;
        } catch (IOException e) {
            log.error("Failed to save resume file", e);
            throw new RuntimeException("Could not store resume file");
        }
    }
}