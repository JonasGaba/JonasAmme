package com.JonasAmme.website.security;

import com.JonasAmme.website.model.User;
import com.JonasAmme.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with the given username");
        }
        user.setUsername(user.getUsername().trim());
        user.setPassword(user.getPassword().trim());
        return new CustomUserDetails(user);
    }

}