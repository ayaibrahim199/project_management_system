package cnfpc.controllers; 

// الكيانات (Entities)
import cnfpc.entities.Tasks;
import cnfpc.entities.Comments;
import cnfpc.entities.Projects; 
import cnfpc.entities.Users;   

//  (Services)
import cnfpc.services.TaskService;
import cnfpc.services.ProjectService; 
import cnfpc.services.UserService;    
import cnfpc.services.CommentService;    
import cnfpc.services.AttachmentService;  

// Spring MVC, Validation, & Security
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*; // 👈 تغطي كل الـ Mappings والـ PathVariable والـ ModelAttribute
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication; // لاستخدام المستخدم اللي عامل Login
import jakarta.validation.Valid; // لـ Validation النماذج

@Controller

@RequestMapping("/tasks")

public class TaskController {



    private final TaskService taskService;

    private final ProjectService projectService;

    private final UserService userService;

    private final CommentService commentService;      

    private final AttachmentService attachmentService; 



    @Autowired

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService,

                          CommentService commentService, AttachmentService attachmentService) {

        this.taskService = taskService;

        this.projectService = projectService;

        this.userService = userService;

        this.commentService = commentService; 

        this.attachmentService = attachmentService; 

    }

    

    // 1. List all tasks (GET)
    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllTasks());
        return "tasks/list";
    }

    // 2. Show create task form (GET /new)
    @GetMapping("/new")
    public String showNewTaskForm(@RequestParam(required = false) Long projectId, Model model) {
        Tasks task = new Tasks();
        
        // If projectId is provided, set it on the task
        if (projectId != null) {
            Projects project = projectService.findProjectById(projectId);
            task.setProject(project);
        }
        
        model.addAttribute("task", task);
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("users", userService.findAllUsers());
        return "tasks/task_form";
    }

    // 3. Show edit task form (GET /edit/{id})
    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Tasks task = taskService.findTaskById(id);
        model.addAttribute("task", task);
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("users", userService.findAllUsers());
        return "tasks/task_form";
    }

    // 4. Save or update task (POST)
    @PostMapping
    public String saveTask(@Valid @ModelAttribute("task") Tasks task,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.findAllProjects());
            model.addAttribute("users", userService.findAllUsers());
            return "tasks/task_form";
        }

        taskService.saveTask(task);
        redirectAttributes.addFlashAttribute("successMessage", "Task saved successfully!");
        return "redirect:/tasks";
    }

    // 5. Delete task (POST)
    @PostMapping("/delete/{id}") 

    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        taskService.deleteTaskById(id);

        redirectAttributes.addFlashAttribute("message", "Task deleted successfully!");

        return "redirect:/tasks";

    }

    

    // 6. Show task details with comments and attachments (GET /{id})
    @GetMapping("/{id}")

    public String showTaskDetails(@PathVariable Long id, Model model) {

        Tasks task = taskService.findTaskById(id);

        model.addAttribute("task", task);

        

        model.addAttribute("comments", commentService.getCommentsByTaskId(id)); 

        

        model.addAttribute("attachments", attachmentService.getAttachmentsByTaskId(id)); 

        

        model.addAttribute("newComment", new Comments()); 

        

        return "tasks/task_details"; 

    }

    

    // 7. Add comment to task (POST)
    @PostMapping("/{taskId}/comments")

    public String addComment(@PathVariable Long taskId, 

                             @Valid @ModelAttribute("newComment") Comments comment,

                             BindingResult result,

                             RedirectAttributes redirectAttributes,

                             Authentication authentication) { 

        

        if (result.hasErrors()) {

             redirectAttributes.addFlashAttribute("error", "Comment cannot be empty!");

             return "redirect:/tasks/" + taskId; 

        }



        Tasks task = taskService.findTaskById(taskId);

        comment.setTask(task);

        

        // Set user if authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Users user = userService.findByUsername(username);
            comment.setUser(user);
        }

        commentService.saveComment(comment);

        redirectAttributes.addFlashAttribute("commentSuccess", "Comment added successfully!");

        return "redirect:/tasks/" + taskId;

    }
}