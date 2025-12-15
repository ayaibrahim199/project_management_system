// cnfpc/services/CustomUserDetailsService.java
package cnfpc.services;

import cnfpc.entities.Users;
import cnfpc.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // الطريقة المطلوبة من واجهة UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // البحث عن المستخدم باستخدام الـ UserRepository اللي عملناها
        Users user = userRepository.findByUsername(username); 
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        
        // تحويل كائن المستخدم إلى كائن UserDetails
        // يتم استخدام كائن Spring Security User البسيط مع قائمة فارغة من الصلاحيات (Authorities)
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            Collections.emptyList() // لا يوجد أدوار (Roles) حالياً
        );
    }
}