package sliit.construction.construction.controller;

import sliit.construction.construction.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService s;
    public ReportController(ReportService s) { this.s = s; }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('PROJECT_MANAGER','SYSTEM_ADMINISTRATOR')")
    public Map<String,Object> summary() { return s.summary(); }
}
