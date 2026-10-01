package sliit.construction.construction.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sliit.construction.construction.dto.NotificationDtos;
import sliit.construction.construction.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // =========================================================
    // CREATE NOTIFICATION
    // =========================================================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response create(@Valid @RequestBody NotificationDtos.Request request) {
        return service.create(request);
    }

    // =========================================================
    // LIST PAGINATED NOTIFICATIONS
    // =========================================================
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<NotificationDtos.Response> list(
            @RequestParam(required = false) Long recipientId,
            @RequestParam(required = false) Boolean read,
            Pageable pageable
    ) {
        return service.list(recipientId, read, pageable);
    }

    // =========================================================
    // LIST ALL NOTIFICATIONS
    // =========================================================
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<NotificationDtos.Response> listAll(
            @RequestParam(required = false) Long recipientId,
            @RequestParam(required = false) Boolean read
    ) {
        return service.listAll(recipientId, read);
    }

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================
    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.UnreadCountResponse getUnreadCount(
            @RequestParam(required = false) Long recipientId
    ) {
        return service.getUnreadCount(recipientId);
    }

    // =========================================================
    // READ ONE NOTIFICATION
    // =========================================================
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response get(@PathVariable Long id) {
        return service.get(id);
    }

    // =========================================================
    // MARK AS READ (SEEN)
    // =========================================================
    @PatchMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response markAsReadPatch(@PathVariable Long id) {
        return service.markAsRead(id);
    }

    @PostMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response markAsReadPost(@PathVariable Long id) {
        return service.markAsRead(id);
    }

    // =========================================================
    // MARK ALL NOTIFICATIONS AS READ (SEEN)
    // =========================================================
    @PostMapping("/mark-all-read")
    @PreAuthorize("isAuthenticated()")
    public void markAllAsRead(@RequestParam(required = false) Long recipientId) {
        service.markAllAsRead(recipientId);
    }

    // =========================================================
    // UPDATE NOTIFICATION
    // =========================================================
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public NotificationDtos.Response update(
            @PathVariable Long id,
            @Valid @RequestBody NotificationDtos.UpdateRequest request
    ) {
        return service.update(id, request);
    }

    // =========================================================
    // DELETE NOTIFICATION
    // =========================================================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // =========================================================
    // CLEAR READ NOTIFICATIONS
    // =========================================================
    @DeleteMapping("/clear-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void clearAll(@RequestParam(required = false) Long recipientId) {
        service.clearAll(recipientId);
    }
}
