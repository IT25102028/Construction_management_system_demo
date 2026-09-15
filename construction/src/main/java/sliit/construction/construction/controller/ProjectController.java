package sliit.construction.construction.controller;

import sliit.construction.construction.dto.ProjectDtos;
import sliit.construction.construction.entity.ProjectStatus;
import sliit.construction.construction.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService s;
    public ProjectController(ProjectService s) { this.s = s; }

    @PostMapping
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ProjectDtos.Response create(@Valid @RequestBody ProjectDtos.Request r) { return s.create(r); }

    @GetMapping
    public Page<ProjectDtos.Response> list(
            @RequestParam(required=false) String search,
            @RequestParam(required=false) ProjectStatus status,
            @RequestParam(required=false) Long managerId,
            Pageable p) {
        if (status != null) return s.listByStatus(status, p);
        if (managerId != null) return s.listByManager(managerId, p);
        return s.list(search, p);
    }

    @GetMapping("/{id}")
    public ProjectDtos.Response get(@PathVariable Long id) { return s.get(id); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ProjectDtos.Response update(@PathVariable Long id, @Valid @RequestBody ProjectDtos.Request r) {
        return s.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public void delete(@PathVariable Long id) { s.delete(id); }
}
