package cnfpc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Comment content cannot be empty")
    private String content;

    private LocalDateTime createdDate = LocalDateTime.now();

    // العلاقة Many-to-One: التعليق ينتمي إلى مهمة واحدة
    @ManyToOne 
    @JoinColumn(name = "task_id")
    private Tasks task;

    // العلاقة Many-to-One: التعليق كتبه مستخدم واحد
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    
    // Constructors, Getters, and Setters...
    public Comments() {}
    public Comments(String content, Tasks task, Users user) {
        this.content = content;
        this.task = task;
        this.user = user;
    }
    public Long getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public Tasks getTask() { return task; }
    public void setTask(Tasks task) { this.task = task; }
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    // ...
}

