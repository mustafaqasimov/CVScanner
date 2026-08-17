package com.project.cvscanner.dto.request;

import com.project.cvscanner.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Schema(description = "Request payload for creating an admin user")
public class CreateAdminRequest {

    @Schema(description = "The username for the admin user")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    String userName;

    @Schema(description = "The email for the admin user")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;

    @Schema(description = "The password for the admin user")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;

    @Schema(description = "The role for the admin user")
    @NotNull(message = "Role is required")
    Role role;
}
