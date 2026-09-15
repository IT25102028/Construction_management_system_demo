package sliit.construction.construction.controller;

import sliit.construction.construction.dto.TaskDtos;
import sliit.construction.construction.entity.TaskStatus;
import sliit.construction.construction.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService s;
    public TaskController(TaskService s) { this.s = s; }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROJECT_MANAGER','SITE_ENGINEER')")
    public TaskDtos.Response create(@Valid @RequestBody TaskDtos.Request r) { return s.create(r); }

    @GetMapping
    public Page<TaskDtos.Response> list(
            @RequestParam(required=false) TaskStatus status,
            @RequestParam(required=false) Long projectId,
            Pageable p) { return s.list(status, projectId, p); }

    @GetMapping("/assigned-to/{userId}")
    public Page<TaskDtos.Response> assigned(@PathVariable Long userId, Pageable p) {
        return s.assignedTo(userId, p);
    }

    @GetMapping("/{id}")
    public TaskDtos.Response get(@PathVariable Long id) { return s.get(id); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROJECT_MANAGER','SITE_ENGINEER')")
    public TaskDtos.Response update(@PathVariable Long id, @Valid @RequestBody TaskDtos.Request r) {
        return s.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROJECT_MANAGER','SITE_ENGINEER')")
    public void delete(@PathVariable Long id) { s.delete(id); }
}
