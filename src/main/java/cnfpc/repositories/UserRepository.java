package cnfpc.repositories;

import cnfpc.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    
    // Spring Data JPA ستنفذ هذه الطريقة تلقائياً بناءً على اسم الحقل
    Users findByUsername(String username);
    Users findByEmail(String email);
}
