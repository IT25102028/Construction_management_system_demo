package sliit.construction.construction.dto;
import sliit.construction.construction.entity.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
public final class TaskDtos {
 private TaskDtos() {}
 public record Request(@NotBlank @Size(max=150) String title,@Size(max=2000) String description,@NotNull LocalDate deadline,
   @NotNull TaskPriority priority,@NotNull TaskStatus status,@NotNull Long projectId,@NotNull Long assigneeId) {}
 public record Response(Long id,String title,String description,LocalDate deadline,TaskPriority priority,TaskStatus status,
   Long projectId,String projectName,Long assigneeId,String assigneeName,LocalDateTime createdAt,LocalDateTime updatedAt) {}
}
