package com.deliverytogo.user_service.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisteredEvent implements Serializable {
    private Long userId;
    private String name;
    private String email;
}