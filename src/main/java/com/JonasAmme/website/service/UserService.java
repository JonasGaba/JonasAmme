package com.JonasAmme.website.service;

import com.JonasAmme.website.model.User;

import java.util.List;


public interface UserService {

    void insertUser(User user);

    List<String> getAllUsernames();

    void deleteUser(String username);

    boolean hasUser(String username);
}


