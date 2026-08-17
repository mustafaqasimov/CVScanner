package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.User;
import com.project.cvscanner.dto.request.CreateAdminRequest;
import com.project.cvscanner.exception.error.ResourceAlreadyExistsException;
import com.project.cvscanner.mapper.UserMapper;
import com.project.cvscanner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminUserService adminUserService;

    @Test
    void createUser_Success() {
        // Given
        CreateAdminRequest request = CreateAdminRequest.builder()
                .userName("admin_user")
                .email("admin@example.com")
                .password("plainPassword123")
                .build();

        String encodedPassword = "encodedPassword123";
        User mappedUser = User.builder()
                .userName("admin_user")
                .email("admin@example.com")
                .password(encodedPassword)
                .build();

        when(userRepository.existsByUserName("admin_user")).thenReturn(false);
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword123")).thenReturn(encodedPassword);
        when(userMapper.toEntityWithRole(request, encodedPassword)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);

        // When & Then
        assertDoesNotThrow(() -> adminUserService.createUser(request));

        // Verifications
        verify(userRepository, times(1)).existsByUserName("admin_user");
        verify(userRepository, times(1)).existsByEmail("admin@example.com");
        verify(passwordEncoder, times(1)).encode("plainPassword123");
        verify(userMapper, times(1)).toEntityWithRole(request, encodedPassword);
        verify(userRepository, times(1)).save(mappedUser);
    }

    @Test
    void createUser_UsernameAlreadyExists_ThrowsException() {
        // Given
        CreateAdminRequest request = CreateAdminRequest.builder()
                .userName("existing_user")
                .email("admin@example.com")
                .password("plainPassword123")
                .build();

        when(userRepository.existsByUserName("existing_user")).thenReturn(true);

        // When & Then
        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> adminUserService.createUser(request)
        );

        assertEquals("Username already taken", exception.getMessage());

        verify(userRepository, times(1)).existsByUserName("existing_user");
        verify(userRepository, never()).existsByEmail(any());
        verifyNoInteractions(passwordEncoder, userMapper);
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        // Given
        CreateAdminRequest request = CreateAdminRequest.builder()
                .userName("admin_user")
                .email("existing@example.com")
                .password("plainPassword123")
                .build();

        when(userRepository.existsByUserName("admin_user")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> adminUserService.createUser(request)
        );

        assertEquals("Email already registered", exception.getMessage());

        verify(userRepository, times(1)).existsByUserName("admin_user");
        verify(userRepository, times(1)).existsByEmail("existing@example.com");
        verifyNoInteractions(passwordEncoder, userMapper);
        verify(userRepository, never()).save(any());
    }
}
