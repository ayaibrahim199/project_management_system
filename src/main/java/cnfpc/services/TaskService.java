package cnfpc.services; // تحتاج لإنشاء هذه الحزمة

import cnfpc.entities.Tasks;
import cnfpc.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // وسم @Service يحدد هذه الفئة كطبقة خدمة في Spring
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired // يقوم Spring بحقن (Dependency Injection) الـ Repository تلقائيًا
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // A. الحصول على جميع المهام
    public List<Tasks> findAllTasks() {
        return taskRepository.findAll();
    }

    // B. الحصول على مهمة واحدة بالمعرّف
    public Tasks findTaskById(Long id) {
        // نستخدم orElseThrow للتعامل مع الحالة التي لا توجد فيها المهمة
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    // C. حفظ أو تحديث مهمة (CRUD Operation) [cite: 92]
    public Tasks saveTask(Tasks task) {
        // يمكنك إضافة منطق أعمال هنا، مثل إرسال إشعار للمستخدم المخصص له.
        return taskRepository.save(task);
    }

    // D. حذف مهمة (CRUD Operation) [cite: 92]
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
    
    // E. استرداد المهام حسب المشروع (باستخدام الطريقة المخصصة في Repository)
    public List<Tasks> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
}