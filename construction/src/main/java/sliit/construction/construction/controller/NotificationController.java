package sliit.construction.construction.controller;

import sliit.construction.construction.dto.NotificationDtos;
import sliit.construction.construction.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService s;
    public NotificationController(NotificationService s) { this.s = s; }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response create(@Valid @RequestBody NotificationDtos.Request r) { return s.create(r); }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<NotificationDtos.Response> list(
            @RequestParam(required=false) Long recipientId,
            @RequestParam(required=false) Boolean read,
            Pageable p) { return s.list(recipientId, read, p); }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response get(@PathVariable Long id) { return s.get(id); }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response update(@PathVariable Long id, @Valid @RequestBody NotificationDtos.UpdateRequest r) {
        return s.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public void delete(@PathVariable Long id) { s.delete(id); }
}
