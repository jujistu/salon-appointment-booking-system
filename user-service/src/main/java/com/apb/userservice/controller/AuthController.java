package com.apb.userservice.controller;

import com.apb.userservice.payload.dto.AuthResponse;
import com.apb.userservice.payload.dto.LoginDTO;
import com.apb.userservice.payload.dto.SignUpDTO;
import com.apb.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpDTO request) throws Exception {
        AuthResponse authResponse = authService.signUp(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO request) throws Exception {
        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/access-token/refresh-token/{refreshToken}")
    public ResponseEntity<AuthResponse> getAccessToken(@PathVariable("refreshToken") String refreshToken) throws Exception {
        AuthResponse response = authService.getAccessTokenFromRefreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
