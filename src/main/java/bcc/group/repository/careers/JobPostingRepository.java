package bcc.group.repository.careers;


import bcc.group.entity.career.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByIsActiveTrue();
    List<JobPosting> findByDepartmentIgnoreCase(String department);
}
