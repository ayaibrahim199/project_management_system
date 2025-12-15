package cnfpc.repositories;

import cnfpc.entities.Teams;
import cnfpc.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Teams, Long> {
    
    Optional<Teams> findByName(String name);
    
    List<Teams> findByTeamLeadId(Long teamLeadId);
    
    @Query("SELECT t FROM Teams t JOIN t.members m WHERE m.id = :userId")
    List<Teams> findTeamsByMemberId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(t) > 0 FROM Teams t JOIN t.members m WHERE t.id = :teamId AND m.id = :userId")
    boolean isUserMemberOfTeam(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
