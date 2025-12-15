// cnfpc/controllers/ProjectController.java - النسخة المصححة والنهائية
package cnfpc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model; 
import cnfpc.services.ProjectService;
import cnfpc.services.TaskService;
import cnfpc.services.TeamService;
import cnfpc.entities.Projects;
import cnfpc.entities.Tasks;
import cnfpc.services.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    
    // حقن الخدمة (Dependency Injection)
    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;
    private final TeamService teamService;
    
    // 👈 Constructor ضروري لـ Dependency Injection
    public ProjectController(ProjectService projectService, UserService userService, TaskService taskService, TeamService teamService) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
        this.teamService = teamService;
    }
    
    // 1. عرض قائمة المشاريع (GET)
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAllProjects());
        return "projects/list"; 
    }
    
    // 1.5. عرض تفاصيل المشروع مع المهام (GET /{id})
    @GetMapping("/{id}")
    public String showProjectDetails(@PathVariable Long id, Model model) {
        Projects project = projectService.findProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("tasks", taskService.getTasksByProjectId(id));
        model.addAttribute("newTask", new Tasks());
        return "projects/projects_details";
    }
    
    // 2. عرض نموذج إضافة/تعديل مشروع (GET /new أو /edit/{id})
    @GetMapping({"/new", "/edit/{id}"})
    public String showProjectForm(@PathVariable(required = false) Long id, Model model) {
        // 🚨 تم الإصلاح: استخدام Projects بدلاً من Project
        Projects project = (id == null) ? new Projects() : projectService.findProjectById(id);
        model.addAttribute("project", project);
        
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("teams", teamService.findAllTeams());
        
        return "projects/project_form"; 
    }
    
    // 3. معالجة الحفظ/التعديل (POST)
    @PostMapping
    // 🚨 تم الإصلاح: استخدام Projects بدلاً من Project
    public String saveProject(@Valid @ModelAttribute("project") Projects project, 
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("teams", teamService.findAllTeams());
            return "projects/project_form";
        }
        
        projectService.saveProject(project);
        redirectAttributes.addFlashAttribute("successMessage", "Project saved!");
        return "redirect:/projects";
    }
    
    // 4. حذف مشروع (DELETE)
    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteProjectById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted!");
        return "redirect:/projects";
    }
}