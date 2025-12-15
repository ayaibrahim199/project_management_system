
package cnfpc.controllers;

import cnfpc.entities.Users;
import cnfpc.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {



    // Registration handlers moved to `RegistrationController` to centralize /register

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new Users());
        return "users/user_form";
    }

    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") Users user,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            return "users/user_form";
        }

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "User saved!");
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Users user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "users/user_form";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUserById(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted!");
        return "redirect:/users";
    }
}