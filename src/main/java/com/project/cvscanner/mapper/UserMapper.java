package com.project.cvscanner.mapper;

import com.project.cvscanner.domain.entities.User;
import com.project.cvscanner.dto.request.CreateAdminRequest;
import com.project.cvscanner.dto.request.RegisterRequest;
import com.project.cvscanner.dto.response.AuthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "password", source = "hashedPassword")
    User toEntity(RegisterRequest request, String hashedPassword);

    @Mapping(target = "token", source = "token")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "role", source = "user.role")
    AuthResponse toResponse(User user, String token);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", source = "hashedPassword")
    User toEntityWithRole(CreateAdminRequest request, String hashedPassword);
}
