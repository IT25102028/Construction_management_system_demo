package sliit.construction.construction.repository;
import sliit.construction.construction.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByAssigneeId(Long userId, Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
}
