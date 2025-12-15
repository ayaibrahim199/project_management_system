package cnfpc.repositories;
import org.springframework.stereotype.Repository;
import cnfpc.entities.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachments, Long> {
    // يمكننا العثور على جميع المرفقات المرتبطة بمهمة معينة
    List<Attachments> findByTaskId(Long taskId);
    

    
}
