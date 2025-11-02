package com.apb.userservice.payload.dto;

import lombok.Data;

@Data
public class KeycloakUserDTO {
    private String username;
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
