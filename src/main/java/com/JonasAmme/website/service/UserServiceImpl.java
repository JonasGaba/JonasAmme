package com.JonasAmme.website.service;

import com.JonasAmme.website.model.User;
import com.JonasAmme.website.repository.UserRepository;
import com.JonasAmme.website.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void insertUser(User user) {
        user.setRole(Role.USER);
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        user.setPassword(bc.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<String> getAllUsernames() {
        List<User> userList = userRepository.findAll();
        return userList.stream().filter(User::isUser).map(User::getUsername).toList();
    }

    @Override
    public void deleteUser(String username) {
        userRepository.delete(userRepository.findByUsername(username));
    }

    @Override
    public boolean hasUser(String username) {
        return userRepository.findByUsername(username) != null;
    }


}


