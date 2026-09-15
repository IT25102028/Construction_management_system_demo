package sliit.construction.construction.controller;

import sliit.construction.construction.dto.UserDtos;
import sliit.construction.construction.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService s;
    public UserController(UserService s) { this.s = s; }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public UserDtos.Response create(@Valid @RequestBody UserDtos.Request r) { return s.create(r); }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public Page<UserDtos.Response> list(@RequestParam(required=false) String search, Pageable p) {
        return s.list(search, p);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR') or authentication.name == @userService.getEntity(#id).username")
    public UserDtos.Response get(@PathVariable Long id) { return s.get(id); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR') or authentication.name == @userService.getEntity(#id).username")
    public UserDtos.Response update(@PathVariable Long id, @Valid @RequestBody UserDtos.Request r) {
        return s.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public void delete(@PathVariable Long id) { s.delete(id); }
}
