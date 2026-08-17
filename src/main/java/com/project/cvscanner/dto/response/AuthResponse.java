package com.project.cvscanner.dto.response;

import com.project.cvscanner.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response payload for authentication")
public class AuthResponse {
    @Schema(description = "The authentication token")
    String token;
    @Schema(description = "The ID of the authenticated user")
    Long userId;
    @Schema(description = "The username of the authenticated user")
    String userName;
    @Schema(description = "The role of the authenticated user")
    Role role;
}
