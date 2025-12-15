package cnfpc.repositories; // تحتاج لإنشاء هذه الحزمة

import org.springframework.stereotype.Repository;
import cnfpc.entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository

public interface ProjectRepository extends JpaRepository<Projects, Long> {




}
