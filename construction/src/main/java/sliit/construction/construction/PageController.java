package sliit.construction.construction;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping({"/", "/login"})
	public String login() {
		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	@GetMapping("/projects")
	public String projects() {
		return "projects";
	}

	@GetMapping("/project-details")
	public String projectDetails() {
		return "project-details";
	}

	@GetMapping("/tasks")
	public String tasks() {
		return "tasks";
	}

	@GetMapping("/progress")
	public String progress() {
		return "progress";
	}

	@GetMapping("/materials")
	public String materials() {
		return "materials";
	}

	@GetMapping("/clients")
	public String clients() {
		return "clients";
	}

	@GetMapping("/users")
	public String users() {
		return "users";
	}

	@GetMapping("/documents")
	public String documents() {
		return "documents";
	}

	@GetMapping("/reports")
	public String reports() {
		return "reports";
	}

	@GetMapping("/notifications")
	public String notifications() {
		return "notifications";
	}

	@GetMapping("/communication")
	public String communication() {
		return "communication";
	}

	@GetMapping("/activity-logs")
	public String activityLogs() {
		return "activity-logs";
	}

	@GetMapping("/settings")
	public String settings() {
		return "settings";
	}
}
