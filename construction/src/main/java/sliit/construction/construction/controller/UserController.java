package sliit.construction.construction.controller;

import sliit.construction.construction.dto.UserDtos;
import sliit.construction.construction.service.UserService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService s;

    public UserController(UserService s) {
        this.s = s;
    }


    // =========================================================
    // CREATE USER
    // =========================================================

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public UserDtos.Response create(
            @Valid @RequestBody UserDtos.Request r) {

        return s.create(r);
    }


    // =========================================================
    // GET ALL USERS (PAGINATED, SEARCHABLE, FILTERABLE)
    // =========================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('SYSTEM_ADMINISTRATOR', 'PROJECT_MANAGER', 'SITE_ENGINEER', 'CONSTRUCTION_SUPERVISOR', 'PROCUREMENT_OFFICER')")
    public Page<UserDtos.Response> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            Pageable p) {

        return s.list(search, role, p);
    }


    // =========================================================
    // GET USER METRICS / STATS
    // =========================================================

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMINISTRATOR', 'PROJECT_MANAGER', 'SITE_ENGINEER', 'CONSTRUCTION_SUPERVISOR', 'PROCUREMENT_OFFICER')")
    public java.util.Map<String, Object> getStats() {
        return s.getUserStats();
    }


    // =========================================================
    // GET PROJECT MANAGERS
    // =========================================================

    @GetMapping("/project-managers")
    @PreAuthorize("isAuthenticated()")
    public Page<UserDtos.Response> getProjectManagers(
            Pageable pageable) {

        return s.getProjectManagers(pageable);
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('SYSTEM_ADMINISTRATOR') " +
                    "or authentication.name == @userService.getEntity(#id).username"
    )
    public UserDtos.Response get(
            @PathVariable Long id) {

        return s.get(id);
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    @PutMapping("/{id}")
    @PreAuthorize(
            "hasRole('SYSTEM_ADMINISTRATOR') " +
                    "or authentication.name == @userService.getEntity(#id).username"
    )
    public UserDtos.Response update(
            @PathVariable Long id,
            @Valid @RequestBody UserDtos.Request r) {

        return s.update(id, r);
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public void delete(
            @PathVariable Long id) {

        s.delete(id);
    }
}