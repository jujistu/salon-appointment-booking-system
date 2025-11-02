package com.apb.userservice.payload.dto;

import com.apb.userservice.model.Role;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String refresh_token;
    private String message;
    private String title;
    private Role role;
}
