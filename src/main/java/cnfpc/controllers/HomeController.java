package cnfpc.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cnfpc.services.UserService;
import cnfpc.services.TaskService;
import cnfpc.services.TeamService;
import cnfpc.services.ProjectService;
import cnfpc.entities.Users;

@Controller
public class HomeController {

    private final UserService userService;
    private final TaskService taskService;
    private final TeamService teamService;
    private final ProjectService projectService;

    public HomeController(UserService userService, TaskService taskService, TeamService teamService, ProjectService projectService) {
        this.userService = userService;
        this.taskService = taskService;
        this.teamService = teamService;
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/home";
        } else {
            return "index";
        }
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "home"; // fallback
        }

        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        // Projects owned/managed by the user
        int projectsCount = 0;
        if (user != null) {
            if (user.getManagedProjects() != null) projectsCount = user.getManagedProjects().size();
        }

        // Teams where the user is a member
        int teamsCount = 0;
        if (user != null) {
            teamsCount = teamService.getTeamsForUser(user.getId()).size();
        }

        // Open tasks assigned to the user (status other than COMPLETED)
        int openTasksCount = 0;
        if (user != null) {
            java.util.List<cnfpc.entities.Tasks> assigned = taskService.getTasksByAssignedUserId(user.getId());
            if (assigned != null) {
                for (cnfpc.entities.Tasks t : assigned) {
                    if (t.getStatus() == null || !t.getStatus().equalsIgnoreCase("COMPLETED")) {
                        openTasksCount++;
                    }
                }
            }
        }

        model.addAttribute("projectsCount", projectsCount);
        model.addAttribute("teamsCount", teamsCount);
        model.addAttribute("openTasksCount", openTasksCount);

        return "home";
    }
}
