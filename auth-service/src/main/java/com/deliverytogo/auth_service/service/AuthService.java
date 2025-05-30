package com.deliverytogo.auth_service.service;

import com.deliverytogo.auth_service.dto.AuthRequest;
import com.deliverytogo.auth_service.dto.AuthResponse;
import com.deliverytogo.auth_service.event.UserRegisteredEvent;
import com.deliverytogo.auth_service.mapper.UserMapper;
import com.deliverytogo.auth_service.messaging.UserEventPublisher;
import com.deliverytogo.auth_service.model.User;
import com.deliverytogo.auth_service.repository.UserRepository;
import com.deliverytogo.auth_service.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       UserEventPublisher userEventPublisher, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userEventPublisher = userEventPublisher;
        this.userMapper = userMapper;
    }

    public AuthResponse register(AuthRequest request) {
        // Use MapStruct to convert DTO to Entity
        User user = userMapper.toEntity(request);

        // Manually handle password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        //Publish UserRegisteredEvent
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
        userEventPublisher.publishUserRegisteredEvent(event);

        return new AuthResponse(jwtUtil.generateToken(user));
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return new AuthResponse(jwtUtil.generateToken(user));
    }
}
