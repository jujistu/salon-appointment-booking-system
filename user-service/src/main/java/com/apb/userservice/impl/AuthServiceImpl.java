package com.apb.userservice.impl;

import com.apb.userservice.model.Role;
import com.apb.userservice.model.User;
import com.apb.userservice.payload.dto.AuthResponse;
import com.apb.userservice.payload.dto.SignUpDTO;
import com.apb.userservice.payload.dto.TokenResponse;
import com.apb.userservice.repository.UserRepository;
import com.apb.userservice.service.AuthService;
import com.apb.userservice.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    public AuthResponse login(String email, String password) throws Exception {
        TokenResponse tokenResponse = keycloakService.getAdminAccessToken(email,password, "password",null);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login Successful");
        return authResponse;
    }

    @Override
    public AuthResponse signUp(SignUpDTO signUpDTO) throws Exception {
        keycloakService.createUser(signUpDTO);
        User user = new User();
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(signUpDTO.getPassword());
        user.setRole(signUpDTO.getRole());
        user.setFullName(signUpDTO.getFullName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        TokenResponse tokenResponse = keycloakService.getAdminAccessToken(signUpDTO.getUsername(),signUpDTO.getPassword(), "password",null);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setRole(user.getRole());
        authResponse.setMessage("Registered Successfully");

        return authResponse;
    }

    @Override
    public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception {
        TokenResponse tokenResponse = keycloakService.getAdminAccessToken(null,null, "refresh_token",refreshToken);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login Successful");
        return authResponse;
    }
}
