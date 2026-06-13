package bcc.group.repository;

import bcc.group.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStatus(String status);
    List<Project> findByProjectType(String projectType);
    List<Project> findByTitleContainingIgnoreCase(String title);
}