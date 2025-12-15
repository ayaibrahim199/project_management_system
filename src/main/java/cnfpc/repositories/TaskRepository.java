package cnfpc.repositories; // تحتاج لإنشاء هذه الحزمة

import cnfpc.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Tasks, Long> {
    
    // مثال على طريقة بحث مخصصة مطلوبة في المشروع [cite: 98]
    // يمكننا العثور على جميع المهام المرتبطة بمشروع معين
    List<Tasks> findByProjectId(Long projectId);
    
    // يمكننا العثور على جميع المهام المسندة لمستخدم معين
    List<Tasks> findByAssignedUserId(Long userId);
}