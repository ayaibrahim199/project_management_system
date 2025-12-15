package cnfpc.repositories;
import org.springframework.stereotype.Repository;
import cnfpc.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    // يمكننا العثور على جميع التعليقات المرتبطة بمهمة معينة
    List<Comments> findByTaskId(Long taskId);
    
    
}
