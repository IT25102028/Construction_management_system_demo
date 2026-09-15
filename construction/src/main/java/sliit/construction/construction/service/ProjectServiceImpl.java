package sliit.construction.construction.service;
import sliit.construction.construction.dto.ProjectDtos; import sliit.construction.construction.entity.*; import sliit.construction.construction.repository.*; import sliit.construction.construction.exception.*;
import org.springframework.data.domain.*; import org.springframework.stereotype.Service;
@Service public class ProjectServiceImpl implements ProjectService {
 private final ProjectRepository repo; private final UserRepository users;
 public ProjectServiceImpl(ProjectRepository repo,UserRepository users){this.repo=repo;this.users=users;}
 public ProjectDtos.Response create(ProjectDtos.Request r){return map(repo.save(build(new Project(),r)));}
 public Page<ProjectDtos.Response> list(String s,Pageable p){return (s==null||s.isBlank()?repo.findAll(p):repo.findByNameContainingIgnoreCase(s,p)).map(this::map);}
 public Page<ProjectDtos.Response> listByStatus(ProjectStatus s,Pageable p){return repo.findByStatus(s,p).map(this::map);}
 public Page<ProjectDtos.Response> listByManager(Long id,Pageable p){return repo.findByManagerId(id,p).map(this::map);}
 public ProjectDtos.Response get(Long id){return map(entity(id));}
 public ProjectDtos.Response update(Long id,ProjectDtos.Request r){return map(repo.save(build(entity(id),r)));}
 public void delete(Long id){repo.delete(entity(id));}
 private Project entity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found: "+id));}
 private Project build(Project p,ProjectDtos.Request r){User m=users.findById(r.managerId()).orElseThrow(()->new ResourceNotFoundException("Manager user not found: "+r.managerId()));
   p.setName(r.name());p.setDescription(r.description());p.setLocation(r.location());p.setStartDate(r.startDate());p.setEndDate(r.endDate());p.setActualEndDate(r.actualEndDate());p.setBudget(r.budget());p.setResourceAllocation(r.resourceAllocation());p.setStatus(r.status());p.setManager(m);return p;}
 private ProjectDtos.Response map(Project p){return new ProjectDtos.Response(p.getId(),p.getName(),p.getDescription(),p.getLocation(),p.getStartDate(),p.getEndDate(),p.getActualEndDate(),p.getBudget(),p.getResourceAllocation(),p.getStatus(),p.getManager().getId(),p.getManager().getFullName(),p.getCreatedAt(),p.getUpdatedAt());}
}
