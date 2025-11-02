package com.apb.userservice.service;

import com.apb.userservice.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserFromJWT(String jwt) throws Exception;
}
