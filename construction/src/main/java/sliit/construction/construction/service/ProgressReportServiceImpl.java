package sliit.construction.construction.service;
import sliit.construction.construction.dto.ProgressDtos; import sliit.construction.construction.entity.*; import sliit.construction.construction.repository.*; import sliit.construction.construction.exception.*;
import org.springframework.data.domain.*; import org.springframework.stereotype.Service;
@Service public class ProgressReportServiceImpl implements ProgressReportService {
 private final ProgressReportRepository repo; private final ProjectRepository projects; private final TaskRepository tasks; private final UserRepository users;
 public ProgressReportServiceImpl(ProgressReportRepository r,ProjectRepository p,TaskRepository t,UserRepository u){repo=r;projects=p;tasks=t;users=u;}
 public ProgressDtos.Response create(ProgressDtos.Request r){return map(repo.save(build(new ProgressReport(),r)));}
 public Page<ProgressDtos.Response> list(Long projectId,Pageable p){return (projectId==null?repo.findAll(p):repo.findByProjectId(projectId,p)).map(this::map);}
 public ProgressDtos.Response get(Long id){return map(entity(id));}
 public ProgressDtos.Response update(Long id,ProgressDtos.Request r){return map(repo.save(build(entity(id),r)));}
 public void delete(Long id){repo.delete(entity(id));}
 private ProgressReport entity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Progress report not found: "+id));}
 private ProgressReport build(ProgressReport x,ProgressDtos.Request r){x.setCompletionPercentage(r.completionPercentage());x.setStatusUpdate(r.statusUpdate());x.setStatus(r.status());
   x.setProject(projects.findById(r.projectId()).orElseThrow(()->new ResourceNotFoundException("Project not found: "+r.projectId())));
   x.setTask(r.taskId()==null?null:tasks.findById(r.taskId()).orElseThrow(()->new ResourceNotFoundException("Task not found: "+r.taskId())));
   x.setReportedBy(users.findById(r.reportedById()).orElseThrow(()->new ResourceNotFoundException("User not found: "+r.reportedById())));return x;}
 private ProgressDtos.Response map(ProgressReport x){return new ProgressDtos.Response(x.getId(),x.getCompletionPercentage(),x.getStatusUpdate(),x.getStatus(),x.getProject().getId(),x.getTask()==null?null:x.getTask().getId(),x.getReportedBy().getId(),x.getReportedBy().getFullName(),x.getCreatedAt(),x.getUpdatedAt());}
}
