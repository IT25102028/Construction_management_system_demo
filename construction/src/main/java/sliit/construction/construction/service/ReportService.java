package sliit.construction.construction.service;

import sliit.construction.construction.repository.*;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ReportService {
    private final ProjectRepository projects;
    private final TaskRepository tasks;
    private final MaterialRepository materials;

    public ReportService(ProjectRepository projects, TaskRepository tasks, MaterialRepository materials) {
        this.projects = projects; this.tasks = tasks; this.materials = materials;
    }

    public Map<String,Object> summary() {
        return Map.of("projects", projects.count(), "tasks", tasks.count(), "materials", materials.count());
    }
}
