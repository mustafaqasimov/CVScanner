package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.User;
import com.project.cvscanner.dto.request.LoginRequest;
import com.project.cvscanner.dto.request.RegisterRequest;
import com.project.cvscanner.dto.response.AuthResponse;
import com.project.cvscanner.exception.error.InvalidCredentialsException;
import com.project.cvscanner.exception.error.ResourceAlreadyExistsException;
import com.project.cvscanner.mapper.UserMapper;
import com.project.cvscanner.repository.UserRepository;
import com.project.cvscanner.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public void register(RegisterRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new ResourceAlreadyExistsException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }
        userRepository.save(
                userMapper.toEntity(request, passwordEncoder.encode(request.getPassword()))
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);
        return userMapper.toResponse(user, token);
    }
}
