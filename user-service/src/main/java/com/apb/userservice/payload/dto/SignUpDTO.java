package com.apb.userservice.payload.dto;

import com.apb.userservice.model.Role;
import lombok.Data;

@Data
public class SignUpDTO {

    private String fullName;
    private String email;
    private String password;
    private String username;
    private Role role;
}
