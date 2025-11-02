package com.apb.userservice.impl;

import com.apb.userservice.model.User;
import com.apb.userservice.payload.dto.KeycloakUserDTO;
import com.apb.userservice.payload.response.UserException;
import com.apb.userservice.repository.UserRepository;
import com.apb.userservice.service.KeycloakService;
import com.apb.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("User Not Found");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new UserException("User with ID " + id + " Not Found!");
        }
        User existingUser = userOptional.get();
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UserException("User with ID " + id + " Not Found!");
        }
        userRepository.deleteById(user.get().getId());
    }

    @Override
    public User getUserFromJWT(String jwt) throws Exception {
        KeycloakUserDTO keycloakUserDTO = keycloakService.fetchUserProfileByJWT(jwt);
        return userRepository.findByEmail(keycloakUserDTO.getEmail());
    }
}
