package com.deliverytogo.user_service.service;

import com.deliverytogo.user_service.dto.UserRequest;
import com.deliverytogo.user_service.model.User;

public interface UserService {
    User createUser(UserRequest request);
    User getUserById(Long id);
}