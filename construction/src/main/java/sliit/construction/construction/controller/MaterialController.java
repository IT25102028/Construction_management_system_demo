package sliit.construction.construction.controller;

import sliit.construction.construction.dto.MaterialDtos;
import sliit.construction.construction.entity.DeliveryStatus;
import sliit.construction.construction.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    private final MaterialService s;
    public MaterialController(MaterialService s) { this.s = s; }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROCUREMENT_OFFICER','PROJECT_MANAGER')")
    public MaterialDtos.Response create(@Valid @RequestBody MaterialDtos.Request r) { return s.create(r); }

    @GetMapping
    public Page<MaterialDtos.Response> list(
            @RequestParam(required=false) String search,
            @RequestParam(required=false) DeliveryStatus status,
            Pageable p) { return s.list(search, status, p); }

    @GetMapping("/{id}")
    public MaterialDtos.Response get(@PathVariable Long id) { return s.get(id); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROCUREMENT_OFFICER','PROJECT_MANAGER')")
    public MaterialDtos.Response update(@PathVariable Long id, @Valid @RequestBody MaterialDtos.Request r) {
        return s.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROCUREMENT_OFFICER','PROJECT_MANAGER')")
    public void delete(@PathVariable Long id) { s.delete(id); }
}
