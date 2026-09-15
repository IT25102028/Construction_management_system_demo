package sliit.construction.construction.service;
import sliit.construction.construction.dto.TaskDtos; import sliit.construction.construction.entity.*; import sliit.construction.construction.repository.*; import sliit.construction.construction.exception.*;
import org.springframework.data.domain.*; import org.springframework.stereotype.Service;
@Service public class TaskServiceImpl implements TaskService {
 private final TaskRepository repo; private final ProjectRepository projects; private final UserRepository users;
 public TaskServiceImpl(TaskRepository r,ProjectRepository p,UserRepository u){repo=r;projects=p;users=u;}
 public TaskDtos.Response create(TaskDtos.Request r){return map(repo.save(build(new Task(),r)));}
 public Page<TaskDtos.Response> list(TaskStatus s,Long projectId,Pageable p){Page<Task> x=projectId!=null?repo.findByProjectId(projectId,p):(s!=null?repo.findByStatus(s,p):repo.findAll(p));return x.map(this::map);}
 public Page<TaskDtos.Response> assignedTo(Long id,Pageable p){return repo.findByAssigneeId(id,p).map(this::map);}
 public TaskDtos.Response get(Long id){return map(entity(id));}
 public TaskDtos.Response update(Long id,TaskDtos.Request r){return map(repo.save(build(entity(id),r)));}
 public void delete(Long id){repo.delete(entity(id));}
 private Task entity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found: "+id));}
 private Task build(Task t,TaskDtos.Request r){t.setTitle(r.title());t.setDescription(r.description());t.setDeadline(r.deadline());t.setPriority(r.priority());t.setStatus(r.status());
   t.setProject(projects.findById(r.projectId()).orElseThrow(()->new ResourceNotFoundException("Project not found: "+r.projectId())));
   t.setAssignee(users.findById(r.assigneeId()).orElseThrow(()->new ResourceNotFoundException("Assignee not found: "+r.assigneeId())));return t;}
 private TaskDtos.Response map(Task t){return new TaskDtos.Response(t.getId(),t.getTitle(),t.getDescription(),t.getDeadline(),t.getPriority(),t.getStatus(),t.getProject().getId(),t.getProject().getName(),t.getAssignee().getId(),t.getAssignee().getFullName(),t.getCreatedAt(),t.getUpdatedAt());}
}
