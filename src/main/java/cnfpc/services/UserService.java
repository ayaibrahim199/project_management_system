
package cnfpc.services;

import org.springframework.stereotype.Service;
import cnfpc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import cnfpc.entities.Users;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder; 

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Find user by username
    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find user by email
    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // A. الحصول على جميع المستخدمين
    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    // B. الحصول على مستخدم واحد بالمعرّف
    public Users findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // C. حفظ أو تحديث مستخدم (CRUD Operation) - مع تشفير كلمة السر
    public Users saveUser(Users user) {
        // 👈 2. منطق التشفير
        // هذا الشرط مهم: لو المستخدم موجود بالفعل وكنا بنعدل بياناته، 
        // ومش عايز يغير كلمة السر (فأرسلها فارغة)، مش بنعيد تشفير كلمة السر القديمة.
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
             // 3. تشفير كلمة السر قبل الحفظ
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        } else if (user.getId() != null) {
            // لو بنعدل مستخدم ومفيش كلمة سر جديدة، نجيب القديمة من DB ونحطها
            Users existingUser = findUserById(user.getId());
            user.setPassword(existingUser.getPassword());
        }
        
        return userRepository.save(user);
    }

    // D. حذف مستخدم (CRUD Operation)
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    
    // E. طريقة أساسية للـ Security
    public Users findByUsername(String username) {
        // نفترض أنك أضفت هذه الطريقة في UserRepository
        return userRepository.findByUsername(username); 
    }
}