package sliit.construction.construction.repository;
import sliit.construction.construction.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);
    Page<Project> findByManagerId(Long managerId, Pageable pageable);
}
