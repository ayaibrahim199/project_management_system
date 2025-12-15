package cnfpc.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "attachments")
public class Attachments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name is required")
    @Size(max = 255)
    private String fileName;

    @NotBlank(message = "File path is required")
    private String filePath; // مسار التخزين الفعلي

    // العلاقة Many-to-One: المرفق ينتمي إلى مهمة واحدة
    @ManyToOne 
    @JoinColumn(name = "task_id")
    private Tasks task;

    // Constructors, Getters, and Setters...
    public Attachments() {}
    public Attachments(String fileName, String filePath, Tasks task) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.task = task;
    }
    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Tasks getTask() { return task; }
    public void setTask(Tasks task) { this.task = task; }
    

}