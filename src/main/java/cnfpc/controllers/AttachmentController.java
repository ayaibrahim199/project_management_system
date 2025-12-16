package cnfpc.controllers;
import cnfpc.entities.Attachments;
import cnfpc.entities.Tasks;
import cnfpc.services.AttachmentService;
import cnfpc.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final TaskService taskService;
    private static final String UPLOAD_DIR = "uploads/attachments/";
    private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);
    public AttachmentController(AttachmentService attachmentService, TaskService taskService) {
        this.attachmentService = attachmentService;
        this.taskService = taskService;
    }
    // Show upload form for a specific task
    @GetMapping("/upload/{taskId}")
    public String showUploadForm(@PathVariable Long taskId, Model model) {
        Tasks task = taskService.findTaskById(taskId);
        model.addAttribute("task", task);
        return "attachments/upload_form";
    }
    // Handle file upload
    @PostMapping("/upload/{taskId}")
    public String uploadAttachment(@PathVariable Long taskId,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a file to upload!");
                return "redirect:/attachments/upload/" + taskId;
            }
           // Create upload directory with proper path handling
            File uploadDirFile = new File(UPLOAD_DIR);
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create upload directory");
                }
            }
            Tasks task = taskService.findTaskById(taskId);
            // Generate unique filename
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                originalFileName = "file";
            }  
            // Remove invalid characters from filename
            String cleanFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFileName = UUID.randomUUID().toString() + "_" + cleanFileName;
            String filePath = UPLOAD_DIR + uniqueFileName;
            // Save file to disk
            Path path = Paths.get(filePath).toAbsolutePath();
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            // Create attachment record
            Attachments attachment = new Attachments();
            attachment.setFileName(originalFileName);
            attachment.setFilePath(filePath);
            attachment.setTask(task);
            attachmentService.saveAttachment(attachment);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
            return "redirect:/tasks/" + taskId;
        } catch (IOException e) {
            logger.error("Failed to upload attachment for task {}", taskId, e);
            redirectAttributes.addFlashAttribute("error", "Failed to upload file: " + e.getMessage());
            return "redirect:/attachments/upload/" + taskId;
        }
    }
    // Download attachment
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) {
        Optional<Attachments> attachment = attachmentService.getAttachmentById(id); 
        if (attachment.isPresent()) {
            Attachments file = attachment.get();
            try {
                Path filePath = Paths.get(file.getFilePath()).toAbsolutePath();
                Resource resource = new FileSystemResource(filePath); 
                if (resource.exists()) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                            .body(resource);
                }
            } catch (Exception e) {
                logger.error("Error while preparing attachment download for id {}", id, e);
            }
        }    
        return ResponseEntity.notFound().build();
    }
    // Delete attachment
    @PostMapping("/delete/{id}")
    public String deleteAttachment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Attachments> attachment = attachmentService.getAttachmentById(id);
        if (attachment.isPresent()) {
            Long taskId = attachment.get().getTask().getId();  
            // Delete file from disk
            try {
                Path filePath = Paths.get(attachment.get().getFilePath()).toAbsolutePath();
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                logger.warn("Failed to delete attachment file for id {}: {}", id, e.getMessage());
            }
            attachmentService.deleteAttachment(id);
            redirectAttributes.addFlashAttribute("message", "File deleted successfully!");
            return "redirect:/tasks/" + taskId;
        }
        redirectAttributes.addFlashAttribute("error", "File not found!");
        return "redirect:/tasks";
    }
}
