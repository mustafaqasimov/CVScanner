package com.project.cvscanner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request payload for user login", name = "LoginRequest")
public class LoginRequest {

    @Schema(description = "The username for the user", example = "johndoe")
    @NotBlank(message = "Username is required")
    String userName;

    @Schema(description = "The password for the user", example = "securePassword123")
    @NotBlank(message = "Password is required")
    String password;
}
