package sliit.construction.construction.repository;
import sliit.construction.construction.entity.ProgressReport;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProgressReportRepository extends JpaRepository<ProgressReport,Long> {
    Page<ProgressReport> findByProjectId(Long projectId, Pageable pageable);
    Page<ProgressReport> findByTaskId(Long taskId, Pageable pageable);
}
