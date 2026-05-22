package bcc.group.services.careerserviceImpl;

import bcc.group.dto.career.CreateJobPostingRequest;
import bcc.group.dto.career.JobPostingResponse;
import bcc.group.entity.career.JobPosting;
import bcc.group.exception.ResourceNotFoundException;
import bcc.group.mapper.careers.JobPostingMapper;
import bcc.group.repository.careers.JobPostingRepository;
import bcc.group.services.careersService.JobPostingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository repository;
    private final JobPostingMapper mapper;

    @Override @Transactional
    public JobPostingResponse create(CreateJobPostingRequest request) {
        JobPosting entity = mapper.toEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Override @Transactional(readOnly = true)
    public List<JobPostingResponse> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<JobPostingResponse> getActive() {
        return repository.findByIsActiveTrue().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public JobPostingResponse getById(Long id) {
        JobPosting entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found"));
        return mapper.toDto(entity);
    }

    @Override @Transactional
    public JobPostingResponse update(Long id, CreateJobPostingRequest request) {
        JobPosting entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found"));
        mapper.updateEntityFromRequest(request, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Job posting not found");
        repository.deleteById(id);
    }
}
