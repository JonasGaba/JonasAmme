package com.JonasAmme.website.controller;

import com.JonasAmme.website.model.User;
import com.JonasAmme.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SecurityController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin/login")
    public String viewAdminLoginPage() {
        return "admin/admin_login";
    }

    @GetMapping("/admin/home")
    public String viewAdminHomePage() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        System.out.println(roles);

        return "admin/admin_home";
    }

    @GetMapping("/user/login")
    public String viewUserLoginPage() {
        return "user/user_login";
    }

    @GetMapping("/user/home")
    public String viewUserHomePage() {
        return "user/user_home";
    }

}
