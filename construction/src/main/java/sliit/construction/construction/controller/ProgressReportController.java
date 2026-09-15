package sliit.construction.construction.controller;

import sliit.construction.construction.dto.ProgressDtos;
import sliit.construction.construction.service.ProgressReportService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress-reports")
public class ProgressReportController {
    private final ProgressReportService s;
    public ProgressReportController(ProgressReportService s) { this.s = s; }

    @PostMapping
    @PreAuthorize("hasAnyRole('SITE_ENGINEER','CONSTRUCTION_SUPERVISOR')")
    public ProgressDtos.Response create(@Valid @RequestBody ProgressDtos.Request r) { return s.create(r); }

    @GetMapping
    public Page<ProgressDtos.Response> list(@RequestParam(required=false) Long projectId, Pageable p) {
        return s.list(projectId, p);
    }

    @GetMapping("/{id}")
    public ProgressDtos.Response get(@PathVariable Long id) { return s.get(id); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SITE_ENGINEER','CONSTRUCTION_SUPERVISOR')")
    public ProgressDtos.Response update(@PathVariable Long id, @Valid @RequestBody ProgressDtos.Request r) {
        return s.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SITE_ENGINEER','CONSTRUCTION_SUPERVISOR')")
    public void delete(@PathVariable Long id) { s.delete(id); }
}
