package sliit.construction.construction.service;
import sliit.construction.construction.dto.TaskDtos;
import sliit.construction.construction.entity.TaskStatus;
import org.springframework.data.domain.*;
public interface TaskService {
 TaskDtos.Response create(TaskDtos.Request r); Page<TaskDtos.Response> list(TaskStatus status,Long projectId,Pageable p);
 Page<TaskDtos.Response> assignedTo(Long userId,Pageable p); TaskDtos.Response get(Long id); TaskDtos.Response update(Long id,TaskDtos.Request r); void delete(Long id);
}
