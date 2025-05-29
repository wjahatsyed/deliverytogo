package com.deliverytogo.user_service.service;

import com.deliverytogo.user_service.dto.UserRequest;
import com.deliverytogo.user_service.model.Address;
import com.deliverytogo.user_service.model.User;
import com.deliverytogo.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        if (request.getAddresses() != null) {
            for (UserRequest.AddressRequest a : request.getAddresses()) {
                Address address = new Address();
                address.setStreet(a.getStreet());
                address.setCity(a.getCity());
                address.setZip(a.getZip());
                address.setUser(user);
                user.getAddresses().add(address);
            }
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
