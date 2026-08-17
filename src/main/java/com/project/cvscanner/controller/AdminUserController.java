package com.project.cvscanner.controller;

import com.project.cvscanner.dto.request.CreateAdminRequest;
import com.project.cvscanner.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin - User Management")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "Create a new user with a specific role (admin only)")
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateAdminRequest request) {
        adminUserService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
