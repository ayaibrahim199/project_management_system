package cnfpc.services;
import cnfpc.entities.Projects;
import cnfpc.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service // وسم @Service يحدد هذه الفئة كطبقة خدمة في Spring
public class ProjectService {


    private final ProjectRepository projectRepository;

    @Autowired // يقوم Spring بحقن (Dependency Injection) الـ Repository تلقائيًا
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // A. الحصول على جميع المشاريع
    public List<Projects> findAllProjects() {
        return projectRepository.findAll();
    }

    // B. الحصول على مشروع واحد بالمعرّف
    public Projects findProjectById(Long id) {
        // نستخدم orElseThrow للتعامل مع الحالة التي لا توجد فيها المشروع
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
    }

    // C. حفظ أو تحديث مشروع (CRUD Operation) [cite: 92]
    public Projects saveProject(Projects project) {
        // يمكنك إضافة منطق أعمال هنا، مثل إرسال إشعار للمستخدم المخصص له.
        return projectRepository.save(project);
    }

    // D. حذف مشروع (CRUD Operation) [cite: 92]
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    // E. Count projects managed by a specific user
    public long countProjectsManagedBy(Long managerId) {
        return projectRepository.countByManagerId(managerId);
    }
}