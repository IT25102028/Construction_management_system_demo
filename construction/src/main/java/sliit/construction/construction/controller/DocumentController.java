package sliit.construction.construction.controller;

import sliit.construction.construction.dto.DocumentDtos;
import sliit.construction.construction.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService s;
    public DocumentController(DocumentService s) { this.s = s; }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROJECT_MANAGER','SITE_ENGINEER','PROCUREMENT_OFFICER','SYSTEM_ADMINISTRATOR')")
    public DocumentDtos.Response create(@Valid @RequestBody DocumentDtos.Request r) { return s.create(r); }

    @GetMapping
    public Page<DocumentDtos.Response> list(
            @RequestParam(required=false) Long projectId,
            @RequestParam(required=false) String search,
            Pageable p) { return s.list(projectId, search, p); }

    @GetMapping("/{id}")
    public DocumentDtos.Response get(@PathVariable Long id) { return s.get(id); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROJECT_MANAGER','SITE_ENGINEER','PROCUREMENT_OFFICER','SYSTEM_ADMINISTRATOR')")
    public DocumentDtos.Response update(@PathVariable Long id, @Valid @RequestBody DocumentDtos.Request r) {
        return s.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROJECT_MANAGER','SITE_ENGINEER','PROCUREMENT_OFFICER','SYSTEM_ADMINISTRATOR')")
    public void delete(@PathVariable Long id) { s.delete(id); }
}
