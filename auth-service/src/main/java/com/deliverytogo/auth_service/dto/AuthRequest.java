package com.deliverytogo.auth_service.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;
}

