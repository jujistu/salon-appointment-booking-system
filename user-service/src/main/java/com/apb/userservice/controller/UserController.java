package com.apb.userservice.controller;


import com.apb.userservice.model.User;
import com.apb.userservice.payload.response.UserException;

import com.apb.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserException {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) throws UserException {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws UserException  {
        userService.deleteUser(id);
        return new ResponseEntity<>("User Deleted Successfully!", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.getUserFromJWT(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
