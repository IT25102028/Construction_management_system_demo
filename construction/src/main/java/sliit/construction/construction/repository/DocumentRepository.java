package sliit.construction.construction.repository;
import sliit.construction.construction.entity.Document;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DocumentRepository extends JpaRepository<Document,Long> {
    Page<Document> findByProjectId(Long projectId, Pageable pageable);
    Page<Document> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
