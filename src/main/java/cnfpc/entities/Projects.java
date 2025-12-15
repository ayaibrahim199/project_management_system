// cnfpc/entities/Projects.java
package cnfpc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

// 1. الإصلاح: استيراد الكيان الصحيح (Tasks)
// حذف: import org.springframework.scheduling.config.Task;
// إضافة:
// يجب أن يكون لديك هذا الكيان في حزمة entities
// إذا كان اسم ملفك هو Tasks.java، فتأكد من استيراده (إذا كنت تستخدمه في نفس الباكج فليس بالضرورة)


@Entity
@Table(name = "projects")
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty") // تحقق من البيانات 
    // 2. الإصلاح: حذف
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") 
    private String title;

    private String description;
    
    // العلاقة هنا: مشروع واحد يمكن أن يحتوي على مهام عديدة (One-to-Many) 
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Tasks> tasks; // 👈 استخدام كيان Tasks الصحيح
    private String status = "ACTIVE"; // الحالة الافتراضية للمشروع

    // العلاقة Many-to-One: مشروع واحد يديره مستخدم واحد
    @ManyToOne 
    @JoinColumn(name = "manager_id")
    private Users manager;

    // العلاقة Many-to-One: مشروع واحد ينتمي لفريق واحد
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Teams team;

    // Constructors, Getters, and Setters...
    public Projects() {}
    public Projects(String title, String description, Users manager) {
        this.title = title;
        this.description = description;
        this.manager = manager;
    }
    
    // تأكد من أن كل الـ Getters والـ Setters مكتوبة بشكل سليم 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } // إضافة Setter للـ Id
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Tasks> getTasks() { return tasks; }
    public void setTasks(List<Tasks> tasks) { this.tasks = tasks; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Users getManager() { return manager; }
    public void setManager(Users manager) { this.manager = manager; }
    public Teams getTeam() { return team; }
    public void setTeam(Teams team) { this.team = team; }
}