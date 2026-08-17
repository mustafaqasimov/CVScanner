package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.User;
import com.project.cvscanner.dto.request.CreateAdminRequest;
import com.project.cvscanner.exception.error.ResourceAlreadyExistsException;
import com.project.cvscanner.mapper.UserMapper;
import com.project.cvscanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void createUser(CreateAdminRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new ResourceAlreadyExistsException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        User user = userMapper.toEntityWithRole(request, passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
