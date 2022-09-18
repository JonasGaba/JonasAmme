package com.JonasAmme.website.controller;

import com.JonasAmme.website.model.User;
import com.JonasAmme.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/home")
    public String viewAdminHomePage() {

        return "admin/admin_home";
    }

    @GetMapping("/login")
    public String viewUserLoginPage() {
        return "login";
    }

    @PostMapping("/logout")
    public String LogoutPost() {
        return "login";
    }


    @GetMapping("/admin/adduser")
    public String showUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "admin/add_user";
    }

    @PostMapping("/admin/adduser")
    public String submitForm(@ModelAttribute("user") User user, Model model) {
        if (userService.hasUser(user.getUsername())) {
            model.addAttribute("error", "That username is already taken");
            return "admin/add_user";
        }
        if (user.getPassword().length() < 7) {
            model.addAttribute("error", "Password must be longer than 7 characters");
            return "admin/add_user";
        }
        userService.insertUser(user);

        return "admin/admin_home";
    }

    @GetMapping("/admin/removeuser")
    public String showUserRemoveForm(Model model) {

        model.addAttribute("users", userService.getAllUsernames());
        model.addAttribute("user", new User());

        return "admin/remove_user.html";
    }

    @PostMapping("/admin/removeuser")
    public String submitDeletion(@ModelAttribute("user") User user) {

        userService.deleteUser(user.getUsername());

        return "admin/admin_home";
    }

}
