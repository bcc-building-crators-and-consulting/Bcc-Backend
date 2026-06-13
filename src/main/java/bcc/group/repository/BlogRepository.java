package bcc.group.repository;

import bcc.group.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findBySlug(String slug);
    List<Blog> findByCategoryIgnoreCase(String category);
    List<Blog> findByIsPublishedTrue();
    List<Blog> findByTitleContainingIgnoreCase(String keyword);
}