// cnfpc/entities/Tasks.java
package cnfpc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import cnfpc.entities.Projects;
import cnfpc.entities.Users;
import java.util.List; // 👈 نحتاج هذه القائمة لإضافة Comments و Attachments

@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Task description is required") 
    // 👈 تم إزالة المسببة للخطأ
    private String description;

    @FutureOrPresent(message = "Deadline must be in the present or future") 
    // 👈 تم إزالة المسببة للخطأ
    private LocalDate deadline;

    private String status = "PENDING"; 

    // العلاقة Many-to-One: مهمة واحدة تنتمي لمشروع واحد
    @ManyToOne 
    @JoinColumn(name = "project_id") 
    private Projects project;

    // العلاقة Many-to-One: مهمة واحدة تسند لمستخدم واحد
    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private Users assignedUser;
    
    // العلاقة One-to-Many: مهمة واحدة لها تعليقات عديدة
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comments> comments; // 👈 يجب التأكد من عمل هذه العلاقة
    
    // العلاقة One-to-Many: مهمة واحدة لها مرفقات عديدة
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Attachments> attachments; // 👈 يجب التأكد من عمل هذه العلاقة

    // Constructors, Getters, and Setters...
    public Tasks() {}
    public Tasks(String description, LocalDate deadline, Projects project, Users assignedUser) {
        this.description = description;
        this.deadline = deadline;
        this.project = project;
        this.assignedUser = assignedUser;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Projects getProject() { return project; }
    public void setProject(Projects project) { this.project = project; }
    public Users getAssignedUser() { return assignedUser; }
    public void setAssignedUser(Users assignedUser) { this.assignedUser = assignedUser; }
    public List<Comments> getComments() { return comments; }
    public void setComments(List<Comments> comments) { this.comments = comments; }
    public List<Attachments> getAttachments() { return attachments; }
    public void setAttachments(List<Attachments> attachments) { this.attachments = attachments; }

}