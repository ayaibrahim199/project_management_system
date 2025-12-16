package cnfpc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    private String password; // سيتم تشفيرها لاحقًا

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    // المستخدم يمكن أن يكون مسؤولاً عن عدة مشاريع (One-to-Many)
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Projects> managedProjects; 

    // المستخدم يمكن أن يكون مكلفًا بعدة مهام (One-to-Many)
    @OneToMany(mappedBy = "assignedUser")
    private List<Tasks> assignedTasks; 
    
    // Constructors (Empty & Full)
    public Users() {}
    public Users(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    

    // Getters and Setters
    // (للاختصار، لن يتم تضمين جميع الـ Getters/Setters هنا، لكن يجب عليك إضافتها)
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Managed projects accessors
    public List<Projects> getManagedProjects() { return managedProjects; }
    public void setManagedProjects(List<Projects> managedProjects) { this.managedProjects = managedProjects; }

    // Assigned tasks accessors
    public List<Tasks> getAssignedTasks() { return assignedTasks; }
    public void setAssignedTasks(List<Tasks> assignedTasks) { this.assignedTasks = assignedTasks; }

}
    

