package com.apb.userservice.service;

import com.apb.userservice.payload.dto.AuthResponse;
import com.apb.userservice.payload.dto.SignUpDTO;

public interface AuthService {
    AuthResponse login(String username, String password) throws Exception;
    AuthResponse signUp(SignUpDTO signUpDTO) throws Exception;
    AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception;
}
