package bcc.group.repository;

import bcc.group.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByCategoryIgnoreCase(String category);
    List<Gallery> findAllByOrderByDisplayOrderAsc();
    List<Gallery> findByIsActiveTrueOrderByDisplayOrderAsc();
}
